const classController = require('../controllers/classController');
const Joi = require('@hapi/joi');

module.exports = [
  {
    method: 'POST',
    path: '/classes',
    handler: classController.addClass,
    options: {
      auth: false, // Aktifkan autentikasi JWT jika diperlukan
    },
  },
  {
    method: 'POST',
    path: '/classes/{classCode}/join',
    handler: classController.joinClass,
    options: {
      auth: false,
      validate: {
        payload: Joi.object({
          studentId: Joi.string().required(),
        }),
        params: Joi.object({
          classCode: Joi.string().required(),
        }),
      },
    },
  },
  {
    method: 'GET',
    path: '/classes',
    handler: classController.getClass,
    options: {
      auth: false, // Menggunakan autentikasi JWT
    },
  },
  {
    method: 'GET',
    path: '/classes/details',
    handler: classController.getClassDetails,
    options: {
      auth: 'jwt', // Jika autentikasi diperlukan
    },
  },
  {
    method: 'POST',
    path: '/classes/{classCode}/predictions',
    handler: classController.addPredictionToClass,
    options: {
      payload: {
        output: 'stream',
        parse: true,
        allow: 'multipart/form-data',
        multipart: true,
        maxBytes: 10485760, // 10 MB
      },
      auth: 'jwt',
    },
  },
];
