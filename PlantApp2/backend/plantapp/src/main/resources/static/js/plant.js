 //Plant class - kept from old code, with small additions

 class Plant {
     constructor(id, name, species, wateringIntervalDays, lastWatered = null) {
         this.id = id;
         this.name = name;
         this.species = species;
         this.wateringIntervalDays = wateringIntervalDays;
         this.lastWatered = lastWatered ? new Date(lastWatered) : null;
     }

     // Sets lastWatered when added to library
     addToLibrary() {
         this.lastWatered = new Date();
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