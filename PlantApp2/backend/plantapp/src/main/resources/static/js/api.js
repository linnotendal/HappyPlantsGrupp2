/**
 * Data layer - all read/write goes through here.
 * Temporarily uses localStorage. When backend/database is ready,
 * swap each method for fetch() (see comments).
 */

const API_BASE_URL = 'http://localhost:8080/api';
const STORAGE_KEY = 'happyplants_library';

const api = {

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
    async getPlantById(id) {
        const response = await fetch(`${API_BASE_URL}/discover/${id}`);

        if (!response.ok) {
            throw new Error("Plant not found");
        }
        return await response.json();
    },

    async getLibrary() {
        try {
            const response = await fetch(`${API_BASE_URL}/user-plants`,{
                credentials: 'include'
            });
            if (!response.ok) throw new Error('Failed to fetch');

            const data = await response.json();
            console.log(data)
            return data.map(plantData => new UserPlant(plantData));
        } catch (error) {
            console.warn('Using localStorage fallback:', error.message);
            return this._getLibraryFromLocalStorage();
        }
    },

    async addToLibrary(plantTemplate) {
        try {
            const response = await fetch(`${API_BASE_URL}/user-plants/add/${plantTemplate.id}`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                credentials: 'include'
            });

            if (!response.ok) throw new Error('Backend not available');
            const data = await response.json();
            return new UserPlant(data);
        } catch (error) {
            console.warn('Using localStorage fallback:', error.message);
        }
    },

    async removeFromLibrary(backendId) {
        try {
            const response = await fetch(`${API_BASE_URL}/user-plants/remove/${backendId}`, {
                method: 'DELETE',
                credentials: 'include'
            });

            if (!response.ok) {
                console.error("Server returned error:", response.status);
                throw new Error('Failed to remove from server');
            }
            return true;
        } catch (error) {
            console.warn('Using localStorage fallback:', error.message);
            this._removeFromLibraryLocalStorage(backendId);
        }
    },

    async waterPlant(userPlantId) {
        try {

            const response = await fetch(`${API_BASE_URL}/user-plants/water/${userPlantId}`, {
                method: 'PUT',
                credentials: 'include'
            });
            if (!response.ok) throw new Error('Failed to water');
            return true;
        } catch (error) {
            console.warn('Using localStorage fallback:', error.message);
        }
    },

    async isInLibrary(plantId) {
        const library = await this.getLibrary();
        return library.some(p => p.id === plantId || p.id === plantId.toString());
    },
/**
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
    }*/
};
