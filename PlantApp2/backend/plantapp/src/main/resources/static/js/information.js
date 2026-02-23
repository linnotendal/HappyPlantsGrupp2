document.addEventListener('DOMContentLoaded', loadPlantInfo);

async function loadPlantInfo() {
    const params = new URLSearchParams(window.location.search);
    const id = params.get('id');
    if (!id) return;

    const container = document.getElementById('plant-info-container');

    try {
        const data = await api.getPlantById(id);
        const plant = new PlantTemplate(data);
        container.innerHTML = plant.renderInformationPage();
    } catch (error) {
        container.innerHTML = "<p>Failed to load plant information.</p>";
        console.error(error);
    }
}
function goBack() {
    window.history.back();
}