/**
 * Data layer - all read/write goes through here.
 * Currently uses localStorage. When backend/database is ready,
 * swap each method for fetch() (see comments).
 */

const STORAGE_KEY = 'happyplants_library';

const api = {

    getLibrary() {
        const data = localStorage.getItem(STORAGE_KEY);
        if (!data) return [];

        return JSON.parse(data).map(p =>
            new Plant(p.id, p.name, p.species, p.wateringIntervalDays, p.lastWatered)
        );
    },

    addToLibrary(plant) {
        const library = this.getLibrary();

        if (library.find(p => p.id === plant.id)) {
            return false;
        }

        plant.addToLibrary();
        library.push(plant);
        this._saveLibrary(library);
        return true;
    },

    removeFromLibrary(plantId) {
        const library = this.getLibrary();
        this._saveLibrary(library.filter(p => p.id !== plantId));
    },

    isInLibrary(plantId) {
        return this.getLibrary().some(p => p.id === plantId);
    },

    _saveLibrary(library) {
        localStorage.setItem(STORAGE_KEY, JSON.stringify(library));
    }
};
