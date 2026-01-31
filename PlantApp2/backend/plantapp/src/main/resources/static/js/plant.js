// Plant model - from old code
class Plant {
    constructor(id, name, species, wateringIntervalDays, lastWatered = new Date()) {
        this.id = id;
        this.name = name;
        this.species = species;
        this.wateringIntervalDays = wateringIntervalDays;
        this.lastWatered = new Date(lastWatered);
    }

    needsWatering() {
        const nextWaterDate = new Date(this.lastWatered);
        nextWaterDate.setDate(nextWaterDate.getDate() + this.wateringIntervalDays);
        return new Date() > nextWaterDate;
    }

    water() {
        this.lastWatered = new Date();
    }

    getDaysSinceWatered() {
        const diffTime = Math.abs(new Date() - this.lastWatered);
        const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
        return diffDays;
    }

    getDaysUntilWatering() {
        const daysSince = this.getDaysSinceWatered();
        return Math.max(0, this.wateringIntervalDays - daysSince);
    }
}