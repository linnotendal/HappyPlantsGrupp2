class UserPlant {
    constructor(data) {
        this.userPlantId = data.userPlantId;
        this.backendId = data.userPlantId;

        this.commonName = data.commonName || 'Unknown Plant';
        this.scientificName = data.scientificName || '';
        this.imageUrl = data.imageUrl || '';
        this.location = data.location || '';
        this.nickName = data.nickName || '';
        this.wateringIntervalDays = data.wateringIntervalDays || 7;
        this.lastWatered = data.lastWatered ? new Date(data.lastWatered) : new Date();
    }

    get name() { return this.nickName || this.commonName; }

    getProgress() {
        const now = new Date();
        const last = new Date(this.lastWatered);

        const diffTime = now.setHours(0,0,0,0) - last.setHours(0,0,0,0);
        const daysSince = Math.floor(diffTime / (1000 * 60 * 60 * 24));
        let progress = 1 - (daysSince / this.wateringIntervalDays);
        return Math.max(0.05, Math.min(1, progress));
    }

    getDaysUntilWatering() {
        const now = new Date();
        const last = new Date(this.lastWatered);
        const diffTime = now.setHours(0,0,0,0) - last.setHours(0,0,0,0);
        const daysSince = Math.floor(diffTime / (1000 * 60 * 60 * 24));
        return this.wateringIntervalDays - daysSince;
    }

    needsWatering() {
        return this.getDaysUntilWatering() <= 0;
    }
    water() {
        this.lastWatered = new Date();
    }
}