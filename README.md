# BASE URL : https://facesignify-capstone-wzoip2su7q-et.a.run.app

### Autentikasi dan Pengelolaan Pengguna

#### 1. Registrasi Pengguna

- *URL:* /register
- *Metode:* POST
- *Deskripsi:* Mendaftarkan pengguna baru.
- *Auth:* Tidak.
- *Request Body:*
  
{
  "username": "string",
  "email": "string",
  "password": "string"
}

#### 2. Login Pengguna

- *URL:* /login/user
- *Metode:* POST
- *Deskripsi:* Login untuk Murid.
- *Auth:* Tidak.
- *Request Body:*
{
  "email": "string"
  "password": "string",
}

#### 3. Login Admin

- *URL:* /login/admin
- *Metode:* POST
- *Deskripsi:* Login untuk Guru.
- *Auth:* Tidak.
- *Request Body:*
{
  "username": "string",
  "password": "string"
}

#### 4. Logout

- *URL:* /logout
- *Metode:* GET
- *Deskripsi:* Logout Admin atau Pengguna.
- *Auth:* Iya(Bearer Token).


#### 5. Mendapatkan Informasi Pengguna

- *URL:* /userinfo/{username}
- *Metode:* GET
- *Deskripsi:* Mendapatkan informasi pengguna berdasarkan username.
- *Auth:* Iya(Bearer Token).
- *Request Params:*
{
  "username": "string",
}

### Manajemen Kelas

#### 6. Menambahkan Kelas

- *URL:* /classes
- *Metode:* POST
- *Deskripsi:* Menambahkan kelas baru.
- *Auth:* Tidak.
- *Request Body:*
{
  "studentId": "string",
  "teacherUsername": "string",
  "subject": "string",
  "className": "string"
}

#### 7. Bergabung Kelas

- *URL:* /classes/{classCode}/join
- *Metode:* POST
- *Deskripsi:* Menambahkan siswa ke dalam kelas yang sudah ada.
- *Auth:* Tidak.
- *Request Params: classCode: string (Kode kelas)*

{
 "studentId": "string"
}

#### 8. Mendapatkan Daftar Kelas

- *URL:* /classes
- *Metode:* GET
- *Deskripsi:* Mendaftarkan daftar semua kelas.
- *Auth:* Tidak.

#### 9. Mendapatkan Detail Kelas

- *URL:* /classes/details
- *Metode:* GET
- *Deskripsi:* Mendapatkan detail dari sebuah kelas.
- *Auth:* Iya(Bearer Token).
- *Request Params:*
{
classCode: string (Kode kelas, opsional)
className: string (Nama kelas, opsional)
subject: string (Subjek kelas, opsional)
}

#### 10. Menambahkan prediksi ke Kelas

- *URL:* /classes/{classCode}/predictions
- *Metode:* POST
- *Deskripsi:* Menambahkan prediksi ke dalam kelas.
- *Auth:* Iya(Bearer Token).
- *Request payload:*
Multipart/form-data dengan field `image` yang berisi file gambar dan field `classCode` yang berisi text kode dari kelas

#### 11. Prediksi Pengenalan Wajah

- **URL:** `/predict`
- **Metode:** `POST`
- **Deskripsi:** Melakukan prediksi pengenalan wajah
- **Auth:** Iya(Bearer Token).
- **Request payload:**
Multipart/form-data dengan field `image` yang berisi file gambar.
