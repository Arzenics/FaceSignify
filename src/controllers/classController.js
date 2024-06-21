const classesService = require('../services/classesService');
const { InputError } = require('../exceptions/InputError');
const faceRecognitionService = require('../services/faceRecognitionService');
const { v4: uuidv4 } = require('uuid'); 

const addClass = async (request, h) => {
  const { studentId, teacherUsername, subject, className } = request.payload;

  try {
    if (!studentId || !teacherUsername || !subject || !className) {
      throw new InputError('Missing required fields');
    }

    const result = await createClass(studentId, teacherUsername, subject, className);

    return h.response({
      status: 'success',
      message: 'Class created successfully',
      data: result,
    }).code(201);
  } catch (error) {
    console.error('Error creating class:', error);
    return h.response({
      status: 'fail',
      message: error.message,
    }).code(500);
  }
};

const joinClass = async (request, h) => {
  const { classCode } = request.params;
  const { studentId } = request.payload;

  try {
    if (!studentId) {
      throw new InputError('Missing student ID');
    }

    await classesService.joinClass(classCode, studentId);

    return h.response({
      status: 'success',
      message: 'Student joined class successfully',
    }).code(200);
  } catch (error) {
    console.error('Error joining class:', error);
    return h.response({
      status: 'fail',
      message: error.message,
    }).code(500);
  }
};

const getClass = async (request, h) => {
  const classes = await classesService.getClasses();
  return h.response({ status: 'success', data: classes }).code(200);
};

const getClassDetails = async (request, h) => {
  try {
    const { classCode, className, subject } = request.query;
    const classDetails = await classesService.getClassDetails({ classCode, className, subject });
    return h.response(classDetails).code(200);
  } catch (error) {
    console.error('Error fetching class details:', error);
    return h.response({ error: error.message }).code(500);
  }
};

const addPredictionToClass = async (request, h) => {
  try {
    const { classCode } = request.params;
    const { image } = request.payload; // Sesuaikan dengan struktur data payload Anda

    // Convert the image file to a buffer
    const imageBuffer = await new Promise((resolve, reject) => {
      const chunks = [];
      image.on('data', (chunk) => chunks.push(chunk));
      image.on('end', () => resolve(Buffer.concat(chunks)));
      image.on('error', reject);
    });

    const fileName = uuidv4();
    const publicUrl = `https://storage.googleapis.com/${process.env.GCLOUD_STORAGE_BUCKET}/images/${fileName}.jpeg`;
    const prediction = await faceRecognitionService.predict(imageBuffer); // Panggil layanan prediksi Anda

    await classesService.addPredictionToClass(classCode, prediction,publicUrl);
    return h.response({ status: 'success', message: 'Prediction added to class' }).code(200);
  } catch (error) {
    console.error('Error adding prediction to class:', error);
    return h.response({ error: error.message }).code(500);
  }
};

module.exports = { addClass,joinClass, getClass, getClassDetails, addPredictionToClass};
