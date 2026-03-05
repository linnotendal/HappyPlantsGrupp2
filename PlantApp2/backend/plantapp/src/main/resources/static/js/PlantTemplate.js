class PlantTemplate{
    constructor({
                    id,
                    common_name,
                    scientific_name,
                    watering,
                    waterFrequencyDays,
                    family,
                    sunlight,
                    default_image,
                    care_level
                }) {
        this.id = id;
        this.common_name = common_name;
        this.scientific_name = scientific_name;
        this.watering = watering;
        this.waterFrequencyDays = waterFrequencyDays;
        this.family = family;
        this.sunlight = sunlight;
        this.default_image = "tempplant.jpg";
        this.care_level=care_level|| "Unknown";
    }


    getWateringDescription() {
        if (!this.waterFrequencyDays || this.waterFrequencyDays <= 0){
         return "Not specified";
         }
        return `Every ${this.waterFrequencyDays} days`;
    }

    getSunlightShort() {
        return this.sunlight || "Not specified";
    }

    hasImage() {
        return !!this.default_image;
    }

    getSunlightShort() {
        return this.sunlight || "Not specified";
    }

    hasImage() {
        return !!this.default_image;
    }
    renderInformationPage() {
        return `
            <div class="info-header">
                <h2 class="info-title">${this.common_name}</h2>
                <p class="info-scientific">${this.scientific_name}</p>
            </div>

            <div class="info-body">
                <div class="info-image">
                    ${this.hasImage()
            ? `<img src="${this.default_image}" alt="${this.common_name}">`
            : `<div class="no-image">🌿</div>`}
                </div>

                <div class="info-details">
                
                    <div class="info-item">
                        <span class="label">Family</span>
                        <span>${this.family || 'N/A'}</span>
                    </div>

                    <div class="info-item">
                      <span class="label">Care level</span>
                      <span>${this.care_level}</span>
                    </div>

                    <div class="info-item">
                        <span class="label">Watering</span>
                        <span>${this.watering || 'Normal'}</span>
                    </div>

                    <div class="info-item">
                        <span class="label">Water Frequency</span>
                        <span>${this.getWateringDescription()}</span>
                    </div>

                    <div class="info-item">
                        <span class="label">Sunlight</span>
                        <span>${this.getSunlightShort()  || "Not specified"}</span>
                    </div>

                </div>
            </div>
        `;
    }
}