//UI logic only. Does not touch data directly, uses api.js for everything.

const DEV_MODE = true;
let currentActiveRoom = "All";
document.addEventListener('DOMContentLoaded', async () => {

    if (!DEV_MODE) {
        try {
            const response = await fetch("/api/users/me", {
                credentials: "include"
            });

            if (!response.ok) {
                window.location.href = "index.html";
                return;
            }

        } catch (error) {
            window.location.href = "index.html";
            return;
        }
    }

    renderDiscoverSection();
    renderLibrarySection();
    setupLogout();
    setupDeleteUser();

    document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape') closePlantModal();
    });

    document.addEventListener('click', (e) => {
        const modal = document.getElementById('plant-modal');
        if (e.target === modal) closePlantModal();
    });

    document.addEventListener('click', () => {
        document.querySelectorAll('.menu-dropdown').forEach(m => m.classList.remove('open'));
    });
});

function showSection(sectionName) {

    document.getElementById('discover-section').classList.add('hidden');
    document.getElementById('library-section').classList.add('hidden');
    document.getElementById('recommended-section').classList.add('hidden');

    document.querySelectorAll('.nav-btn')
        .forEach(btn => btn.classList.remove('active'));

    if (sectionName === 'discover') {

        document.getElementById('discover-section').classList.remove('hidden');
        document.querySelector('.nav-btn[onclick*="discover"]').classList.add('active');
        renderDiscoverSection();

    } else if (sectionName === 'library') {

        currentActiveRoom = "All";
        document.getElementById('library-section').classList.remove('hidden');
        document.querySelector('.nav-btn[onclick*="library"]').classList.add('active');
        renderLibrarySection();

    } else if (sectionName === 'recommended') {

        document.getElementById('recommended-section').classList.remove('hidden');
        document.querySelector('.nav-btn[onclick*="recommended"]').classList.add('active');
        renderRecommendedSection();

    }
}
async function renderRecommendedSection() {

    const grid = document.getElementById('recommended-grid');
    grid.innerHTML = "<p>Loading recommendations...</p>";

    try {
        const contentSuggestion = await api.getContentSuggestion();
        const popularSuggestion = await api.getPopularSuggestion();

        
        const suggestions = [
            { title: "Try something new!", data: contentSuggestion },
            { title: "Popular among users", data: popularSuggestion }
        ].filter(s => s.data);

        
        grid.innerHTML = "";

        const cards = await Promise.all(suggestions.map(async (suggestion) => {
                const plantId = suggestion.data.id ?? suggestion.data.plantId;
                const count = await api.getNbrOfUsersWithThisPlant(plantId);

            const card = document.createElement("div");
            card.className = "plant-card custom-layout";

            card.innerHTML = `
                <h2>${suggestion.title}</h2>
                
                <div class="plant-image-container">
                    ${
                        suggestion.data.imageUrl
                        ? `<img src="${suggestion.data.imageUrl}" alt="${suggestion.data.commonName}">`
                        : `<div class="no-image">🌿</div>`
                    }
                </div>

                <h3>${suggestion.data.commonName}</h3>
                
                <p class="plant-count">${count} users have this plant!</p>
                <div class="card-btn-row">
                    <button class="btn-add">+ Add to My Plants</button>
                </div>
            `;

            const btn = card.querySelector(".btn-add");

            btn.addEventListener("click", () => {
                openPlantAddModal(suggestion.data);
            });

            return card;
        }));

        cards.forEach(card => grid.appendChild(card));

    } catch (error) {
        console.error(error);
        grid.innerHTML = "<p>Failed to load recommendations.</p>";
    }
}

async function handleRecommendedSearch() {

    const query = document.getElementById("plant-search-input").value.trim();

    if (query === "") {
        renderRecommendedSection();
        return;
    }

    const plants = await api.searchPlantsFromBackend(query);
    renderRecommendedSection(plants);
}

async function renderDiscoverSection(plants = null) {
    const grid = document.getElementById('discover-grid');
    const emptyMsg = document.getElementById('empty-library-msg');
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
            addBtn.addEventListener('click', () => openPlantAddModal(plant));
            grid.appendChild(card);
        });
    } catch (error) {
        grid.innerHTML = '<p>Error loading plants from server.</p>';
        grid.innerHTML = `<p>Error: ${error.message}</p>`;
    }
}

async function handleSearch() {
    const query = document.getElementById('plant-search-input').value.trim();
    if (query === "") {
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
    btn.innerHTML = '<span class="btn-spinner"></span> Adding...';
    btn.disabled = true;
    try {
        const plantTemplate = new PlantTemplate(plantData);
        await api.addToLibrary(plantTemplate);
        btn.innerText = "✓ Added";
        btn.classList.remove('btn-add');
        btn.classList.add('btn-added');
        renderLibrarySection();
    } catch (error) {
        btn.innerText = originalText;
        btn.disabled = false;
        console.error("Error adding plant:", error);
        alert("Failed to add plant. Please try again.");
    }
}

function setupLogout() {
    const logoutBtn = document.getElementById("logout-btn");

    if (!logoutBtn) return;

    logoutBtn.addEventListener("click", async () => {
        try {
            const response = await fetch("/api/users/logout", {
                method: "POST",
                credentials: "include"
            });

            if (response.ok) {
                window.location.href = "index.html";
            } else {
                alert("Logout failed");
            }
        } catch (error) {
            console.error(error);
        }
    });
}

function setupDeleteUser() {
    const deleteBtn = document.getElementById("delete-btn");

    if (!deleteBtn) return;

    deleteBtn.addEventListener("click", async () => {
        if(!confirm("Are you sure you want to delete your account?")) {return;}
        try {
            const response = await fetch("/api/users/delete", {
                method: "DELETE",
                credentials: "include"
            });

            console.log(response.text)

            if (response.ok) {
                window.location.href = "index.html";
            } else {
                alert("Account deletion failed.");
            }
        } catch (error) {
            console.error(error);
        }
    });
}

function renderRoomTabs(library) {
    const container = document.getElementById("room-tabs");
    container.innerHTML = "";
    const rooms = [...new Set(
        library
            .map(p => p.location)
            .filter(r => r && r !== "")
    )];
    const allTabs = ["All", ...rooms];
    allTabs.forEach(room => {

        const tab = document.createElement("button");
        tab.className = "room-tab";
        if (room === currentActiveRoom) {
            tab.classList.add("active");
        }
        tab.textContent = room;

        tab.onclick = () => {
            currentActiveRoom = room;
            filterLibraryByRoom(room);
        };

        container.appendChild(tab);
    });
}

async function filterLibraryByRoom(room) {
    currentActiveRoom = room;
    const library = await api.getLibrary();
    if (room === "All") {
        renderLibrarySection(library);
        return;
    }
    const filtered = library.filter(p => p.location === room);
    renderLibrarySection(filtered);
}

async function renderLibrarySection(filteredPlants = null) {
    const grid = document.getElementById('library-grid');
    const emptyMsg = document.getElementById('empty-library-msg');
    const sortSelect = document.getElementById('sort-library-select');
    if (filteredPlants == null) {
        grid.innerHTML = '<p>Loading...</p>';
    }
    const fullLibrary = await api.getLibrary();
    renderRoomTabs(fullLibrary);

    const library = filteredPlants || fullLibrary;

    if (library && library.length > 0) {
        const sortBy = sortSelect.value;

        if (sortBy === 'water-status') {
            library.sort((a, b) => {
                const progressA = typeof a.getProgress === 'function' ? a.getProgress() : 0;
                const progressB = typeof b.getProgress === 'function' ? b.getProgress() : 0;
                return progressA - progressB;
            });
        } else {
            library.sort((a, b) => {
                const nameA = a.name || "";
                const nameB = b.name || "";
                return nameA.localeCompare(nameB);
            });
        }
    }

    grid.innerHTML = '';

    if (library.length === 0) {
        emptyMsg.classList.remove('hidden');
        const query = document.getElementById('library-search-input')?.value;
        emptyMsg.innerText = query ? `No plants found matching "${query}"` : "Your library is empty.";
        return;
    }
    emptyMsg.classList.add('hidden');

    library.forEach(plant => {
        const daysUntil = plant.getDaysUntilWatering();
        const needsWater = plant.needsWatering();
        const progress = plant.getProgress();

        let barColor = '#6b9c38';
        if (progress < 0.5) barColor = '#e6a817';
        if (progress < 0.25) barColor = '#d44';

        let statusText = `Needs water in ${daysUntil} day${daysUntil !== 1 ? 's' : ''}`;
        if (needsWater) statusText = 'Needs water now!';

        grid.innerHTML += `
            <div class="plant-card ${needsWater ? 'needs-water' : ''}">
            
                <div class="card-menu">
                        <button class="menu-btn" onclick="toggleMenu(event, ${plant.userPlantId})">⋮</button>                    
                        <div class="menu-dropdown" id="menu-${plant.userPlantId}">
                        <button onclick="openPlantEditModal(${plant.userPlantId})">Edit</button>
                        <button onclick="removeFromLibrary(${plant.userPlantId})">Remove</button>
                    </div>
                </div>
            
                <h3 class="plant-link" onclick="openPlantInfo(${plant.plantId})">${plant.name}</h3>
                    <p><strong>Placement:</strong> ${plant.location || 'Unknown'}</p> 
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
                    <button class="btn-water" data-tooltip="Mark as watered today" onclick="waterPlant(${plant.userPlantId})">💧 Water</button>
                </div>
            
            </div>
            `;
    });
}

async function handleLibrarySearch() {
    const searchInput = document.getElementById('library-search-input');
    const query = searchInput.value.toLowerCase().trim();

    let library = await api.getLibrary();
    const filteredResults = library.filter(plant => {
        const isInRoom = (currentActiveRoom === "All" || plant.location === currentActiveRoom);
        const matchesQuery = !query ||
            (plant.name && plant.name.toLowerCase().includes(query)) ||
            (plant.family && plant.family.toLowerCase().includes(query));

        return isInRoom && matchesQuery;
    });
    renderLibrarySection(filteredResults);
    document.getElementById('library-search-input').focus();
}
async function handleSortChange() {
    const sortBy = document.getElementById('sort-library-select').value;
    let library = await api.getLibrary();

    if (currentActiveRoom !== "All") {
        library = library.filter(p => p.location === currentActiveRoom);
    }

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
    if (e) {
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
    datareturn = await api.addToLibrary(plant);
    console.log("return from api " +datareturn);
    renderDiscoverSection();
    renderLibrarySection();
}

async function removeFromLibrary(backendId) {
    if (!confirm("Are you sure you want to remove this plant from your library?")) return;
    try {
        const success = await api.removeFromLibrary(backendId);
        if (success) {
            console.log("Removed from server successfully");
            const updatedLibrary = await api.getLibrary();
            if (currentActiveRoom !== "All") {
                const filtered = updatedLibrary.filter(p => p.location === currentActiveRoom);
                renderLibrarySection(filtered);
            } else {
                renderLibrarySection(updatedLibrary);
            }
            showSuccessNotification("Plant removed successfully");        }
    } catch (error) {
        console.error("Failed to remove plant:", error);
    }
}


async function waterPlant(userPlantId) {
    try {
        const success = await api.waterPlant(userPlantId);
        if (success) {
            console.log("Watering successes");
            const updatedLibrary = await api.getLibrary();
            if (currentActiveRoom !== "All") {
                const filtered = updatedLibrary.filter(p => p.location === currentActiveRoom);
                renderLibrarySection(filtered);
            } else {
                renderLibrarySection(updatedLibrary);
            }
            showSuccessNotification("Plant watered! 💧");
            //await renderLibrarySection();
        }
    } catch (error) {
        console.error("Failed to water plant:", error);
    }
}

async function openPlantInfo(id) {

    const container = document.getElementById('plant-info-container');

    try {
        const data = await api.getPlantById(id);
        const plant = new PlantTemplate(data);

        container.innerHTML = plant.renderInformationPage();

        document.getElementById('plant-modal')
            .classList.remove('hidden');

    } catch (error) {
        container.innerHTML = "<p>Failed to load plant information.</p>";
        console.error(error);
    }
}

function closePlantModal() {
    document.getElementById('plant-modal')
        .classList.add('hidden');
}
function openPlantAddModal(plantData) {
    selectedPlantData = plantData;

    document.getElementById('modal-location').value = "Add a nickname (optional)";
    document.getElementById('modal-nickname').value = "";
    document.getElementById('modal-plant-name').innerText = plantData.common_name;
    document.getElementById('plant-add-modal').classList.remove('hidden');
}

function closePlantAddModal() {
    document.getElementById('plant-add-modal').classList.add('hidden');
}
function showSuccessNotification(message) {
    let container = document.querySelector('.toast-container');
    if (!container) {
        container = document.createElement('div');
        container.className = 'toast-container';
        document.body.appendChild(container);
    }
    const toast = document.createElement('div');
    toast.className = 'toast-message';
    toast.innerHTML = `🌿 <span>${message}</span>`;

    container.appendChild(toast);

    setTimeout(() => {
        toast.remove();
        if (container.childNodes.length === 0) container.remove();
    }, 3000);
}
document.getElementById('modal-add-btn').addEventListener('click', async () => {
    const location = document.getElementById('modal-location').value.trim();
    const nickname = document.getElementById('modal-nickname').value.trim();

    try {
        const savedPlant = await api.addToLibrary(selectedPlantData, location);
        console.log(savedPlant);
        if (nickname) {
            await api.setNickName(savedPlant.plantId, nickname);
        }

        showSuccessNotification(`${selectedPlantData.common_name} added to your library!`);
        closePlantAddModal();
        renderLibrarySection();
    } catch (error) {
        console.error(error);
        alert("Failed to add plant. Please try again.");
    }
});

function openPlantEditModal(plantId) {
    userPlantId = plantId;

    document.getElementById('modal-edit-location').value = "";
    document.getElementById('modal-edit-nickname').value = "";
    document.getElementById('plant-edit-modal').classList.remove('hidden');
}

function closePlantEditModal() {
    document.getElementById('plant-edit-modal').classList.add('hidden');
}

document.getElementById('modal-edit-btn').addEventListener('click', async () => {
    const location = document.getElementById('modal-edit-location').value.trim();
    const nickname = document.getElementById('modal-edit-nickname').value.trim();

    try {
        const savedPlant = await api.editUserPlant(userPlantId, nickname, location);

        showSuccessNotification(`Plant edited!`);
        closePlantEditModal();
        renderLibrarySection();
    } catch (error) {
        console.error(error);
        alert("Failed to edit plant. Please try again.");
    }
});