from fastapi import FastAPI, File, UploadFile
from io import BytesIO
from PIL import Image
import torch
from torchvision import transforms
import random
import numpy as np

app = FastAPI()

random.seed(42)
np.random.seed(42)
torch.manual_seed(42)
if torch.cuda.is_available():
    torch.cuda.manual_seed(42)

device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
model = torch.jit.load("new_model.pt", map_location=device)
model.eval()

classes = [
    "Apple___Apple_scab",
    "Pepper,_bell___Bacterial_spot",
    "Corn_(maize)___Common_rust_",
    "Strawberry___Leaf_scorch",
    "Pepper,_bell___healthy",
    "Tomato___Spider_mites Two-spotted_spider_mite",
    "Peach___healthy",
    "Tomato___Tomato_Yellow_Leaf_Curl_Virus",
    "Grape___Esca_(Black_Measles)",
    "Tomato___Early_blight",
    "Cherry_(including_sour)___Powdery_mildew",
    "Corn_(maize)___healthy",
    "Soybean___healthy",
    "Strawberry___healthy",
    "Apple___Cedar_apple_rust",
    "Tomato___Septoria_leaf_spot",
    "Tomato___Bacterial_spot",
    "Grape___Black_rot",
    "Cherry_(including_sour)___healthy",
    "Potato___Late_blight",
    "Corn_(maize)___Northern_Leaf_Blight",
    "Peach___Bacterial_spot",
    "Corn_(maize)___Cercospora_leaf_spot Gray_leaf_spot",
    "Grape___Leaf_blight_(Isariopsis_Leaf_Spot)",
    "Blueberry___healthy",
    "Tomato___healthy",
    "Apple___healthy",
    "Tomato___Leaf_Mold",
    "Tomato___Tomato_mosaic_virus",
    "Squash___Powdery_mildew",
    "Orange___Haunglongbing_(Citrus_greening)",
    "Raspberry___healthy",
    "Tomato___Late_blight",
    "Grape___healthy",
    "Potato___Early_blight",
    "Apple___Black_rot",
    "Potato___healthy",
    "Tomato___Target_Spot"
]

def transform_image(image_bytes: bytes):
    transform = transforms.Compose([
        transforms.ToTensor()
    ])
    image = Image.open(BytesIO(image_bytes)).convert("RGB")
    return transform(image).unsqueeze(0)

@app.post("/predict")
async def predict(file: UploadFile = File(...)):
    try:
        image_bytes = await file.read()
        input_tensor = transform_image(image_bytes).to(device)

        with torch.no_grad():
            output = model(input_tensor)
        predicted_class_idx = int(torch.argmax(output, dim=1).item())
        predicted_class_name = classes[predicted_class_idx]

        return {"predicted_class": predicted_class_name}
    except Exception as e:
        return {"error": str(e)}


if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)
