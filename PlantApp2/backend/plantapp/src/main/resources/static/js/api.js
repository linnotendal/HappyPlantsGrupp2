/**
 * Data layer - all read/write goes through here.
 * Temporarily uses localStorage. When backend/database is ready,
 * swap each method for fetch() (see comments).
 */

//Fetches API key from key.json
/*
async function fetchKey() {
    let key = await fetch("/js/key.json").then((res) => res.json());
    return key.apikey;
}
*/

const API_BASE_URL = 'http://localhost:8080/api';
const STORAGE_KEY = 'happyplants_library';

const api = {

    //GET call to perenual (temporarily only calls first page, 30 plants)
    /*
    async getPlantsFromAPI() {
        const response = await fetch('https://perenual.com/api/v2/species-list?page=1&indoor=1&key=' + await fetchKey())
        var plants = await response.json();

        if (response.ok) {
            return plants.data;
        } else {
            console.log(response);
            return response;
        }
    },
    */
    async searchPlantsFromBackend(query) {
        try {
            const response = await fetch(`http://localhost:8080/api/discover/search?name=${encodeURIComponent(query)}`);
            if (!response.ok) throw new Error('Failed to connect with server');
            return await response.json();
        } catch (error) {
            console.error('Error searching plants:', error);
            return [];
        }
    },
    async getPlantsFromAPI() {
        try {
            const response = await fetch(`${API_BASE_URL}/discover`);
            const data = await response.json();

            if (response.ok) {
                return data;
            } else {
                console.error("Server response fail", response.status);
                return [];
            }
        } catch (error) {
            console.error("Connection with backend failed", error);
            return [];
        }
    },

    async getLibrary() {
        try {
            const response = await fetch(`${API_BASE_URL}/library`);
            if (!response.ok) throw new Error('Backend not available');

            const data = await response.json();
            return data.map(p => {
                const plant = new Plant(
                    parseInt(p.plantId) || p.id,
                    p.nickname,
                    p.family,
                    p.waterFrequencyDays,
                    p.lastWatered
                );
                plant.backendId = p.id;
                return plant;
            });
        } catch (error) {
            console.warn('Using localStorage fallback:', error.message);
            return this._getLibraryFromLocalStorage();
        }
    },

    async addToLibrary(plant) {
        try {
            const response = await fetch(`${API_BASE_URL}/library`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    nickname: plant.name,
                    plantId: plant.id.toString(),
                    waterFrequencyDays: plant.wateringIntervalDays,
                    lastWatered: new Date().toISOString().split('T')[0]
                })
            });

            if (!response.ok) throw new Error('Backend not available');
            return true;
        } catch (error) {
            console.warn('Using localStorage fallback:', error.message);
            return this._addToLibraryLocalStorage(plant);
        }
    },

    async removeFromLibrary(plantId) {
        try {
            const library = await this.getLibrary();
            const plant = library.find(p => p.id === plantId);

            if (!plant || !plant.backendId) {
                throw new Error('Plant not found or no backend ID');
            }

            const response = await fetch(`${API_BASE_URL}/library/${plant.backendId}`, {
                method: 'DELETE'
            });
            if (!response.ok) throw new Error('Failed to remove');
        } catch (error) {
            console.warn('Using localStorage fallback:', error.message);
            this._removeFromLibraryLocalStorage(plantId);
        }
    },

    async waterPlant(plantId) {
        try {
            const library = await this.getLibrary();
            const plant = library.find(p => p.id === plantId);

            if (!plant || !plant.backendId) {
                throw new Error('Plant not found or no backend ID');
            }

            const response = await fetch(`${API_BASE_URL}/library/${plant.backendId}/water`, {
                method: 'PUT'
            });
            if (!response.ok) throw new Error('Failed to water');
        } catch (error) {
            console.warn('Using localStorage fallback:', error.message);
            this._waterPlantLocalStorage(plantId);
        }
    },

    async isInLibrary(plantId) {
        const library = await this.getLibrary();
        return library.some(p => p.id === plantId || p.id === plantId.toString());
    },

    //Localstorage, can be removed when database is implemented, or keep as backup
    _getLibraryFromLocalStorage() {
        const data = localStorage.getItem(STORAGE_KEY);
        if (!data) return [];
        return JSON.parse(data).map(p =>
            new Plant(p.id, p.name, p.species, p.wateringIntervalDays, p.lastWatered)
        );
    },

    _addToLibraryLocalStorage(plant) {
        const library = this._getLibraryFromLocalStorage();
        if (library.find(p => p.id === plant.id)) return false;

        plant.addToLibrary();
        library.push(plant);
        this._saveLibrary(library);
        return true;
    },

    _removeFromLibraryLocalStorage(plantId) {
        const library = this._getLibraryFromLocalStorage();
        this._saveLibrary(library.filter(p => p.id !== plantId));
    },

    _waterPlantLocalStorage(plantId) {
        const library = this._getLibraryFromLocalStorage();
        const plant = library.find(p => p.id === plantId);
        if (plant) {
            plant.lastWatered = new Date();
            this._saveLibrary(library);
        }
    },

    _saveLibrary(library) {
        localStorage.setItem(STORAGE_KEY, JSON.stringify(library));
    }
};
