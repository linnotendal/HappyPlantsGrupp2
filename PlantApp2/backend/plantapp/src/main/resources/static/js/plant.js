 //Plant class - kept from old code, with small additions

 class Plant {
     constructor(id, name, species, wateringIntervalDays, lastWatered = null) {
         this.id = id;
         this.name = name;
         this.species = species;
         this.wateringIntervalDays = wateringIntervalDays;
         this.lastWatered = lastWatered ? new Date(lastWatered) : null;
     }

     addToLibrary() {
         this.lastWatered = new Date();
     }

     //Watering logic from old code
     needsWatering() {
         if (!this.lastWatered) return false;
         const nextWaterDate = new Date(this.lastWatered);
         nextWaterDate.setDate(nextWaterDate.getDate() + this.wateringIntervalDays);
         return new Date() > nextWaterDate;
     }

     water() {
         this.lastWatered = new Date();
     }

     getDaysSinceWatered() {
         if (!this.lastWatered) return null;
         const diffTime = Math.abs(new Date() - this.lastWatered);
         return Math.floor(diffTime / (1000 * 60 * 60 * 24));
     }

     getDaysUntilWatering() {
         if (!this.lastWatered) return null;
         const daysSince = this.getDaysSinceWatered();
         return this.wateringIntervalDays - daysSince;
     }

     getProgress() {
         if (!this.lastWatered) return 1.0;
         const daysSince = this.getDaysSinceWatered();
         let progress = 1.0 - (daysSince / this.wateringIntervalDays);
         if (progress <= 0.02) return 0.02;
         if (progress >= 0.95) return 1.0;
         return progress;
     }
 }

 const HARDCODED_PLANTS = [
    new Plant(1, "Monstera",        "Monstera deliciosa",         7),
    new Plant(2, "Snake Plant",     "Sansevieria trifasciata",   14),
    new Plant(3, "Pothos",          "Epipremnum aureum",          5),
    new Plant(4, "Peace Lily",      "Spathiphyllum",             10),
    new Plant(5, "Rubber Plant",    "Ficus elastica",             9),
    new Plant(6, "Fiddle Leaf Fig", "Ficus lyrata",               7),
    new Plant(7, "ZZ Plant",        "Zamioculcas zamiifolia",    21),
    new Plant(8, "Spider Plant",    "Chlorophytum comosum",       5)
];