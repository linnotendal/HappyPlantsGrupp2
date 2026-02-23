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

async function renderDiscoverSection(plants = null) {
    const grid = document.getElementById('discover-grid');
    grid.innerHTML = '<p>Loading...</p>';

    try {
        if (plants === null) {
            plants = await api.getPlantsFromAPI();
        }
        grid.innerHTML = '';
        if (!plants || plants.length === 0) {
            grid.innerHTML = '<p>No plants found.</p>';
            return;
        }
        plants.forEach(plantData => {
            const plant = new PlantTemplate(plantData);
            const card = document.createElement('div');
            card.className = 'plant-card custom-layout';
            const safeSpecies = plant.scientific_name.replace(/'/g, "\\'");

            card.innerHTML += `
                <h3 class="plant-title plant-link" onclick="openPlantInfo(${plant.id})">
                    ${plant.common_name} </h3>                    
                    <div class="plant-image-container">
                        ${plant.default_image
                ? `<img src="${plant.default_image}" alt="${plant.common_name}">`
                : `<div class="no-image">🌿</div>`}
                    </div>
                    <div class="plant-details">
                        <p><strong>Scientific:</strong><br>${plant.scientific_name}</p>
                        <p><strong>Family:</strong><br>${plant.family || 'N/A'}</p>
                        <p><strong>Water:</strong><br>${plant.watering || 'Normal'}</p>
                    </div>
                    <div class="card-btn-row">
                        <button class="btn-add"> +                   Add to My Plants </button>
                    </div>
                </div>
            `;
            const addBtn = card.querySelector('.btn-add');
            addBtn.addEventListener('click', (event) => handleAddFromDiscover(event, plant));

            grid.appendChild(card);
        });
    } catch (error) {
        grid.innerHTML = '<p>Error loading plants from server.</p>';
        grid.innerHTML = `<p>Error: ${error.message}</p>`;
    }
}
async function handleSearch() {
    const query = document.getElementById('plant-search-input').value.trim();
    if (query==="") {
        const allPlants = await api.getPlantsFromAPI();
        renderDiscoverSection(allPlants);
        return;
    }
    const plants = await api.searchPlantsFromBackend(query);
    renderDiscoverSection(plants);
}
/**
 * this Method adds a plant from discover section to user library
 * @param event
 * @param id
 * @param name
 * @param species
 * @returns {Promise<void>}
 */
async function handleAddFromDiscover(event, plantData) {
    const btn = event.currentTarget;
    const originalText = btn.innerText;
    btn.innerText = "Adding...";
    btn.disabled = true;
    try {
        // ADD some attributes then
        const plantTemplate = new PlantTemplate(plantData);
        await api.addToLibrary(plantTemplate);
        btn.innerText = "✓ Added";
        btn.classList.remove('btn-add');
        btn.classList.add('btn-added');
        renderLibrarySection();
    }catch (error){
        btn.innerText = originalText;
        btn.disabled = false;
        console.error("Error adding plant:", error);
        alert("Failed to add plant. Please try again.");
    }
}

async function renderLibrarySection(filteredPlants = null) {
    const grid = document.getElementById('library-grid');
    const emptyMsg = document.getElementById('empty-library-msg');
    if (filteredPlants==null){
        grid.innerHTML = '<p>Loading...</p>';
    }
    const library = filteredPlants || await api.getLibrary();
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
                    <button class="menu-btn" onclick="toggleMenu(event, ${plant.userPlantId})">⋮</button>
                    <div class="menu-dropdown" id="menu-${plant.userPlantId}">
                        <button onclick="editPlant(${plant.userPlantId})">Edit</button>
                        <button onclick="removeFromLibrary(${plant.userPlantId})">Remove</button>
                    </div>
                </div>
            
                <h3>${plant.name}</h3>
                    <p><strong>Family:</strong> ${plant.family || 'Unknown'}</p>    
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
                    <button class="btn-water" onclick="waterPlant(${plant.userPlantId})">💧 Water</button>
                </div>
            
            </div>
            `;
    });
}
async function handleLibrarySearch() {
    const query = document.getElementById('library-search-input').value.toLowerCase().trim();
    const allLibraryPlants = await api.getLibrary();
    if (query === "") {
        renderLibrarySection(allLibraryPlants);
        return;
    }
    const filtered = allLibraryPlants.filter(plant => {
        const nameMatch = plant.name.toLowerCase().includes(query);
        const familyMatch = plant.family && plant.family.toLowerCase().includes(query);
        return nameMatch || familyMatch;
    });
    renderLibrarySection(filtered);
}
async function handleSortChange() {
    const sortBy = document.getElementById('sort-library-select').value;
    let library = await api.getLibrary();

    if (!library || library.length === 0) return;

    if (sortBy === 'water-status') {
        library.sort((a, b) => {
            const progressA = typeof a.getProgress === 'function' ? a.getProgress() : 0;
            const progressB = typeof b.getProgress === 'function' ? b.getProgress() : 0;
            return progressA - progressB;
        });
    }
    else if (sortBy === 'name') {
        library.sort((a, b) => {
            const nameA = a.name || "";
            const nameB = b.name || "";
            return nameA.localeCompare(nameB);
        });
    }
    renderLibrarySection(library);
}

function toggleMenu(e, id) {
    if(e){
        e.stopPropagation()
        e.preventDefault();
    }
    console.log("Opening menu for plant ID:", id);
    document.querySelectorAll('.menu-dropdown').forEach(m => {
        if (m.id !== `menu-${id}`) m.classList.remove('open');
    });

    const menu = document.getElementById(`menu-${id}`)
    if (menu) {
        menu.classList.toggle('open');
    } else {
        console.error("DOM Element not found: ", `menu-${id}`);
    }
    console.log("Looking for:", `menu-${id}`);
    console.log(document.getElementById(`menu-${id}`));
}

async function addToLibrary(plantId) {
    const source = HARDCODED_PLANTS.find(p => p.id === plantId);
    if (!source) return;

    const plant = new Plant(source.id, source.name, source.species, source.wateringIntervalDays);
    await api.addToLibrary(plant);

    renderDiscoverSection();
    renderLibrarySection();
}

async function removeFromLibrary(backendId) {
    try {
        const success = await api.removeFromLibrary(backendId);
        if (success) {
            console.log("Removed from server successfully");
            await api.getLibrary();
            await renderLibrarySection();
        }
    } catch (error) {
        console.error("Failed to remove plant:", error);
    }
}


async function waterPlant(userPlantId) {
    try {
        const success = await api.waterPlant(userPlantId);
        if (success) {
            console.log("Watering successes")
            await renderLibrarySection();
        }
    } catch (error) {
        console.error("Failed to water plant:", error);
    }
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

function openPlantInfo(id) {
    window.location.href = `information.html?id=${id}`;
}