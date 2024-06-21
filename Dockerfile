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
ENV PORT
ENV JWT_SECRET
ENV MODEL_URL
ENV GOOGLE_CLOUD_PROJECT
ENV GOOGLE_APPLICATION_CREDENTIALS
ENV GCLOUD_STORAGE_BUCKET

# Expose the port your app runs on
EXPOSE 8080

# Perintah untuk menjalankan aplikasi
CMD ["npm", "start"]
