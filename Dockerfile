# Gunakan image Node.js 20 resmi sebagai base image
FROM node:20

# Tentukan working directory dalam container
WORKDIR /app

# Salin file package.json dan package-lock.json ke working directory
COPY package*.json ./

# Install dependencies
RUN npm install

# Salin semua file dari proyek ke working directory
COPY . .

# Set environment variables
ENV PORT=8080
ENV JWT_SECRET=55cafe06710ca272a4d5cc135b28311dcea7384db2877756711b296f8df06f089905c3a94cb681d56829894ad111eed8a2916b5120a3d3b19f1b56e95f3b7684
ENV MODEL_URL=https://storage.googleapis.com/facesignify-bucket-capstone/models/model.json
ENV GOOGLE_CLOUD_PROJECT=capstone-facesignify
ENV GOOGLE_APPLICATION_CREDENTIALS=/app/src/config/serviceAccountKey.json
ENV GCLOUD_STORAGE_BUCKET=gs://capstone-facesignify.appspot.com

# Expose the port your app runs on
EXPOSE 8080

# Perintah untuk menjalankan aplikasi
CMD ["npm", "start"]
