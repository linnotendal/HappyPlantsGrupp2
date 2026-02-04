
//UI logic only. Does not touch data directly - uses api.js for everything.


document.addEventListener('DOMContentLoaded', () => {
    renderDiscoverSection();
    renderLibrarySection();
});

function showSection(sectionName) {
    document.getElementById('discover-section').classList.add('hidden');
    document.getElementById('library-section').classList.add('hidden');
    document.querySelectorAll('.nav-btn').forEach(btn => btn.classList.remove('active'));

    if (sectionName === 'discover') {
        document.getElementById('discover-section').classList.remove('hidden');
        document.querySelectorAll('.nav-btn')[0].classList.add('active');
        renderDiscoverSection();
    } else {
        document.getElementById('library-section').classList.remove('hidden');
        document.querySelectorAll('.nav-btn')[1].classList.add('active');
        renderLibrarySection();
    }
}

function renderDiscoverSection() {
    const grid = document.getElementById('discover-grid');
    grid.innerHTML = '';

    HARDCODED_PLANTS.forEach(plant => {
        const inLibrary = api.isInLibrary(plant.id);

        grid.innerHTML += `
            <div class="plant-card">
                <h3>${plant.name}</h3>
                <p><strong>Species:</strong> ${plant.species}</p>
                <p><strong>Watering:</strong> every ${plant.wateringIntervalDays} days</p>
                <div class="card-btn-row">
                    ${inLibrary
            ? `<button class="btn-added" disabled>✓ Added</button>`
            : `<button class="btn-add" onclick="addToLibrary(${plant.id})">+ Add to My Plants</button>`
        }
                </div>
            </div>
        `;
    });
}

function renderLibrarySection() {
    const grid = document.getElementById('library-grid');
    const emptyMsg = document.getElementById('empty-library-msg');
    const library = api.getLibrary();

    grid.innerHTML = '';

    if (library.length === 0) {
        emptyMsg.classList.remove('hidden');
        return;
    }
    emptyMsg.classList.add('hidden');

    library.forEach(plant => {
        grid.innerHTML += `
            <div class="plant-card">
                <h3>${plant.name}</h3>
                <p><strong>Species:</strong> ${plant.species}</p>
                <p><strong>Watering frequency:</strong> every ${plant.wateringIntervalDays} days</p>
                <p class="added-date">Added: ${plant.lastWatered ? new Date(plant.lastWatered).toLocaleDateString('en-US') : 'Unknown'}</p>
                
                <div class="card-btn-row">
                    <button class="btn-remove" onclick="removeFromLibrary(${plant.id})">🗑️ Remove</button>
                </div>
            </div>
        `;
    });
}

function addToLibrary(plantId) {
    const source = HARDCODED_PLANTS.find(p => p.id === plantId);
    if (!source) return;

    const plant = new Plant(source.id, source.name, source.species, source.wateringIntervalDays);
    api.addToLibrary(plant);

    renderDiscoverSection();
}

function removeFromLibrary(plantId) {
    api.removeFromLibrary(plantId);
    renderLibrarySection();
    renderDiscoverSection();
}