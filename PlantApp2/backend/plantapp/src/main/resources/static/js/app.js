// Main application logic
let plants = [];

async function init() {
    console.log('Initializing Plant Care App...');
    await loadPlants();
    setupEventListeners();
}

async function loadPlants() {
    plants = await api.getPlants();
    renderPlants();
}

function renderPlants() {
    const container = document.getElementById('plants-container');

    if (plants.length === 0) {
        container.innerHTML = '<p>No plants yet. Add your first plant below! 🌱</p>';
        return;
    }

    container.innerHTML = plants.map(plant => `
        <div class="plant-card">
            <h3>${plant.name}</h3>
            <p><strong>Species:</strong> ${plant.species}</p>
            <p><strong>Watering interval:</strong> ${plant.wateringIntervalDays} days</p>
            <p><strong>Last watered:</strong> ${plant.getDaysSinceWatered()} days ago</p>
            <p><strong>Next watering:</strong> ${plant.needsWatering() ? '⚠️ Water now!' : `In ${plant.getDaysUntilWatering()} days`}</p>
            <button onclick="waterPlant(${plant.id})">💧 Water</button>
        </div>
    `).join('');
}

async function waterPlant(plantId) {
    await api.waterPlant(plantId);
    await loadPlants(); // Reload to update UI
}

function setupEventListeners() {
    const form = document.getElementById('plant-form');
    form.addEventListener('submit', async (e) => {
        e.preventDefault();

        const name = document.getElementById('plant-name').value;
        const species = document.getElementById('plant-species').value;
        const interval = parseInt(document.getElementById('watering-interval').value);

        await api.addPlant(name, species, interval);
        await loadPlants();

        form.reset();
    });
}

document.addEventListener('DOMContentLoaded', init);