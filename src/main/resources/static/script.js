document.getElementById('predictionForm').addEventListener('submit', async function(e) {
  e.preventDefault();

  const formData = new FormData(this);
  const jsonData = {};

  for (const [key, value] of formData.entries()) {
    jsonData[key] = value;
  }

  try {
    const response = await fetch('http://localhost:8080/api/predict', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(jsonData)
    });

    if (!response.ok) throw new Error('Error al obtener respuesta del servidor');

    const data = await response.json();
    document.getElementById('result').style.display = 'block';
    document.getElementById('predictionValue').textContent = data.prediction;
    document.getElementById('suggestionValue').textContent = data.suggestion;
  } catch (error) {
    alert('Ocurrió un error: ' + error.message);
  }
});
