class UserPlant {
    constructor(data) {
        this.plantId = data.plantId;
        this.commonName = data.commonName;
        this.scientificName = data.scientificName;
        this.imageUrl = data.imageUrl;
        this.lastWatered = new Date(data.lastWatered);
        this.nickName = data.nickName;
        this.wateringIntervalDays = data.wateringIntervalDays;
    }

    getProgress() {
        const daysSince = this.getDaysSinceWatered();
        let progress = 1 - (daysSince / this.template.wateringIntervalDays);
        if (progress <= 0.02) return 0.02;
        if (progress >= 0.95) return 1.0;
        return progress;
    }

    getDaysSinceWatered() {
        const diff = new Date() - this.lastWatered;
        return Math.floor(diff / (1000 * 60 * 60 * 24));
    }
}