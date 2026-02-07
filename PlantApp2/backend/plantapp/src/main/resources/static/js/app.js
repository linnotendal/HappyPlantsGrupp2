//UI logic only. Does not touch data directly, uses api.js for everything.

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

async function renderDiscoverSection() {
    const grid = document.getElementById('discover-grid');
    grid.innerHTML = '<p>Loading...</p>';

    const checks = await Promise.all(
        HARDCODED_PLANTS.map(p => api.isInLibrary(p.id))
    );

    grid.innerHTML = '';

    //Puts plants from API in discover section
    /*
    var plants = await api.getPlantsFromAPI();

    if (plants.status) {
        grid.innerHTML = `<p>API Http response: ${plants.status}<p>`
    } else {
            plants.forEach(plant => {

                grid.innerHTML += `
                <div class="plant-card">
                ${
                    (document.getElementById('discover-grid').classList.contains('grid-view'))
                    ? (plant.default_image != null)
                        ? `<img src=${plant.default_image.regular_url} alt=${plant.common_name} style="border-radius:5px;">` 
                        : `<div class="no-image"></div>`
                    : ''
                }
                    <h3>${plant.common_name}</h3>
                    <p><strong>Species:</strong> ${plant.scientific_name[0]}</p>
                    <p><strong>Genus:</strong> ${plant.genus}</p>
                </div>
             `;
    })}
    */

    HARDCODED_PLANTS.forEach((plant, index) => {
        const inLibrary = checks[index];


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

async function renderLibrarySection() {
    const grid = document.getElementById('library-grid');
    const emptyMsg = document.getElementById('empty-library-msg');

    grid.innerHTML = '<p>Loading...</p>';

    const library = await api.getLibrary();

    grid.innerHTML = '';

    if (library.length === 0) {
        emptyMsg.classList.remove('hidden');
        return;
    }
    emptyMsg.classList.add('hidden');

    library.forEach(plant => {
        const daysUntil = plant.getDaysUntilWatering();
        const needsWater = plant.needsWatering();
        const progress = plant.getProgress();

        let barColor = '#6b9c38';
        if (progress < 0.5)  barColor = '#e6a817';
        if (progress < 0.25) barColor = '#d44';

        let statusText = `Needs water in ${daysUntil} day${daysUntil !== 1 ? 's' : ''}`;
        if (needsWater) statusText = 'Needs water now!';

        grid.innerHTML += `
<div class="plant-card ${needsWater ? 'needs-water' : ''}">

    <div class="card-menu">
        <button class="menu-btn" onclick="toggleMenu(event, ${plant.id})">⋮</button>
        <div class="menu-dropdown" id="menu-${plant.id}">
            <button onclick="editPlant(${plant.id})">Edit</button>
            <button onclick="removeFromLibrary(${plant.id})">Remove</button>
        </div>
    </div>

    <h3>${plant.name}</h3>
    <p><strong>Species:</strong> ${plant.species}</p>

    <div class="progress-container">
        <div class="progress-bar">
            <div class="progress-fill"
                 style="width:${progress * 100}%; background-color:${barColor};"></div>
        </div>
        <p class="water-status ${needsWater ? 'urgent' : ''}">
            ${statusText}
        </p>
    </div>

    <p class="last-watered">
        Last watered:
        ${plant.lastWatered
            ? new Date(plant.lastWatered).toLocaleDateString('en-US')
            : 'Never'}
    </p>

    <div class="card-btn-row">
        <button class="btn-water" onclick="waterPlant(${plant.id})">💧 Water</button>
    </div>

</div>
`;
    });
}

function toggleMenu(e, id) {
    e.stopPropagation()

    document.querySelectorAll('.menu-dropdown')
        .forEach(m => m.classList.remove('open'))

    const menu = document.getElementById(`menu-${id}`)
    menu.classList.toggle('open')
}

async function addToLibrary(plantId) {
    const source = HARDCODED_PLANTS.find(p => p.id === plantId);
    if (!source) return;

    const plant = new Plant(source.id, source.name, source.species, source.wateringIntervalDays);
    await api.addToLibrary(plant);

    renderDiscoverSection();
    renderLibrarySection();
}

async function removeFromLibrary(plantId) {
    await api.removeFromLibrary(plantId);
    renderLibrarySection();
    renderDiscoverSection();
}

async function waterPlant(plantId) {
    await api.waterPlant(plantId);
    renderLibrarySection();
}

let gridActive = false

function toggleGrid(section) {
    const grid =
        section === 'discover'
            ? document.getElementById('discover-grid')
            : document.getElementById('library-grid')

    grid.classList.toggle('grid-view');
    (section =='discover') ? renderDiscoverSection() : renderLibrarySection();
}