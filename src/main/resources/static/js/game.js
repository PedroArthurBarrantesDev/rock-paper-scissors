// Talks to the Java game through the REST API and draws the result.
// The browser never picks the computer's move or decides who wins.

const API = '/api/match';
const SHUFFLE_MS = 900;
const SHUFFLE_STEP_MS = 120;
const CHOICES = ['ROCK', 'PAPER', 'SCISSORS'];

const cards = document.querySelectorAll('.card');
const newGameButton = document.getElementById('new-game');
const resultLine = document.getElementById('result');
const playerScore = document.getElementById('player-score');
const computerScore = document.getElementById('computer-score');
const drawsScore = document.getElementById('draws');
const playerHand = document.getElementById('player-hand');
const computerHand = document.getElementById('computer-hand');
const playerPick = document.getElementById('player-pick');
const computerPick = document.getElementById('computer-pick');

// The card images in the page are the single place image paths live.
const images = {};
cards.forEach(card => {
    images[card.dataset.choice] = card.querySelector('img').getAttribute('src');
});

const reducedMotion = window.matchMedia('(prefers-reduced-motion: reduce)').matches;

function label(choice) {
    return choice.charAt(0) + choice.slice(1).toLowerCase();
}

function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

function showPick(img, choice) {
    if (choice) {
        img.src = images[choice];
        img.alt = label(choice);
        img.hidden = false;
    } else {
        img.removeAttribute('src');
        img.alt = '';
        img.hidden = true;
    }
}

function setCardsEnabled(enabled) {
    cards.forEach(card => {
        card.disabled = !enabled;
    });
}

function setResult(text, kind) {
    resultLine.textContent = text;
    resultLine.className = 'result' + (kind ? ' ' + kind : '');
    if (kind) {
        // Restart the pop animation
        void resultLine.offsetWidth;
        resultLine.classList.add('pop');
    }
}

function renderMatch(match) {
    playerScore.textContent = match.playerWins;
    computerScore.textContent = match.computerWins;
    drawsScore.textContent = match.draws;
    setCardsEnabled(!match.over);
    newGameButton.classList.toggle('attention', match.over);
}

function matchOverMessage(match) {
    return match.result === 'WIN'
        ? `You won the match ${match.playerWins} to ${match.computerWins}!`
        : `The computer won the match ${match.computerWins} to ${match.playerWins}.`;
}

function roundMessage(round) {
    const you = label(round.player);
    const cpu = label(round.computer);
    switch (round.outcome) {
        case 'WIN': return `${you} beats ${cpu}. You win the round!`;
        case 'LOSE': return `${cpu} beats ${you}. The computer wins the round.`;
        default: return `Both picked ${you}. It's a draw.`;
    }
}

async function shuffleComputerHand() {
    if (reducedMotion) {
        return;
    }
    computerHand.classList.add('shuffling');
    const end = Date.now() + SHUFFLE_MS;
    let i = 0;
    while (Date.now() < end) {
        showPick(computerPick, CHOICES[i++ % CHOICES.length]);
        await sleep(SHUFFLE_STEP_MS);
    }
    computerHand.classList.remove('shuffling');
}

async function request(method, url, body) {
    const response = await fetch(url, {
        method,
        headers: body ? { 'Content-Type': 'application/json' } : {},
        body: body ? JSON.stringify(body) : undefined
    });
    const data = await response.json().catch(() => ({}));
    if (!response.ok) {
        const error = new Error(data.error || `Request failed with status ${response.status}`);
        error.status = response.status;
        throw error;
    }
    return data;
}

async function playRound(choice, card) {
    setCardsEnabled(false);
    cards.forEach(c => c.classList.toggle('selected', c === card));
    playerHand.classList.remove('winner');
    computerHand.classList.remove('winner');
    showPick(playerPick, choice);
    setResult('The computer is choosing...');

    try {
        // Ask the server and run the shuffle animation at the same time
        const [data] = await Promise.all([
            request('POST', `${API}/rounds`, { choice }),
            shuffleComputerHand()
        ]);
        const { round, match } = data;

        showPick(computerPick, round.computer);
        if (round.outcome === 'WIN') {
            playerHand.classList.add('winner');
        } else if (round.outcome === 'LOSE') {
            computerHand.classList.add('winner');
        }

        if (match.over) {
            setResult(`${roundMessage(round)} ${matchOverMessage(match)}`, round.outcome.toLowerCase());
        } else {
            setResult(roundMessage(round), round.outcome.toLowerCase());
        }
        renderMatch(match);
    } catch (error) {
        showPick(computerPick, null);
        setResult(error.message, 'lose');
        // The server is the source of truth, so resync with it
        await loadMatch();
    } finally {
        cards.forEach(c => c.classList.remove('selected'));
    }
}

async function loadMatch() {
    try {
        const match = await request('GET', API);
        renderMatch(match);
        if (match.over) {
            setResult(matchOverMessage(match), match.result === 'WIN' ? 'win' : 'lose');
        }
    } catch (error) {
        setResult('Could not reach the game server. Is it running?', 'lose');
        setCardsEnabled(false);
    }
}

async function newGame() {
    try {
        const match = await request('POST', API);
        showPick(playerPick, null);
        showPick(computerPick, null);
        playerHand.classList.remove('winner');
        computerHand.classList.remove('winner');
        setResult('New match! Pick a card to play the first round.');
        renderMatch(match);
    } catch (error) {
        setResult(error.message, 'lose');
    }
}

cards.forEach(card => {
    card.addEventListener('click', () => playRound(card.dataset.choice, card));
});
newGameButton.addEventListener('click', newGame);

loadMatch();
