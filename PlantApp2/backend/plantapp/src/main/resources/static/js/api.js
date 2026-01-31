// API service for communicating with Spring Boot backend
const API_BASE_URL = 'http://localhost:8080/api';

const api = {
    async getPlants() {
        try {
            const response = await fetch(`${API_BASE_URL}/plants`);
            if (!response.ok) throw new Error('Failed to fetch plants');
            const data = await response.json();
            return data.map(p => new Plant(p.id, p.name, p.species, p.wateringIntervalDays, p.lastWatered));
        } catch (error) {
            console.error('Error fetching plants:', error);
            // Fallback: use localStorage for now
            return this.getPlantsFromLocalStorage();
        }
    },

    async addPlant(name, species, wateringIntervalDays) {
        try {
            const response = await fetch(`${API_BASE_URL}/plants`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ name, species, wateringIntervalDays })
            });
            if (!response.ok) throw new Error('Failed to add plant');
            const data = await response.json();
            return new Plant(data.id, data.name, data.species, data.wateringIntervalDays, data.lastWatered);
        } catch (error) {
            console.error('Error adding plant:', error);
            // Fallback: use localStorage
            return this.addPlantToLocalStorage(name, species, wateringIntervalDays);
        }
    },

    async waterPlant(plantId) {
        try {
            const response = await fetch(`${API_BASE_URL}/plants/${plantId}/water`, {
                method: 'PUT'
            });
            if (!response.ok) throw new Error('Failed to water plant');
            return await response.json();
        } catch (error) {
            console.error('Error watering plant:', error);
            this.waterPlantInLocalStorage(plantId);
        }
    },
};