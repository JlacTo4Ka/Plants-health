FROM python:3.10-slim

RUN apt-get update && apt-get install -y --no-install-recommends \
    build-essential \
    libgl1-mesa-glx \
    libglib2.0-0 && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY requirements.txt ./

RUN pip install --upgrade pip && \
    pip install --no-cache-dir -r requirements.txt --extra-index-url https://download.pytorch.org/whl/cpu

COPY . .

RUN test -f plant-disease-model.pth || echo "Warning: model file not found!"

EXPOSE 8000
CMD ["uvicorn", "handler:app", "--host", "0.0.0.0", "--port", "8000"]
