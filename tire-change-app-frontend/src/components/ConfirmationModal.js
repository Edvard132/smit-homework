import React, { useEffect, useState } from 'react';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import { getReadableDate } from '../utils/date';
import api from '../api/axiosConfig';
import ConfirmationBody from './ConfirmationBody';
import { IoIosCheckmarkCircle } from 'react-icons/io';

const ConfirmationModal = ({ modalShow, setModalShow, event, getAllAvailableTimes }) => {
  const [contactInformation, setContactInformation] = useState('');
  const [errMessage, setErrMessage] = useState('');
  const [response, setResponse] = useState({
    time: '',
    address: '',
    vehicleType: '',
  });
  const [isLoading, setIsLoading] = useState(false);
  const onCloseModal = () => {
    setErrMessage('');
    setContactInformation('');
    setResponse({
      time: '',
      address: '',
      vehicleType: '',
    });
    setModalShow(false);
  };

  const bookServiceCity = async (workshopId, id) => {
    setIsLoading(true);
    try {

      const response = await api.post(`/api/v1/workshops/${workshopId}/bookTime/${id}`, {
        contactInformation: contactInformation,
      });
      const data = await response.data;

      setResponse({
        time: getReadableDate(data.time),
        address: data.address,
        vehicleType: data.vehicleType,
      });
    } catch (err) {
      setErrMessage(err.response.data.message);
    } finally {
      setIsLoading(false);
    }
  };

  const bookService = async () => {
    if (contactInformation === '') {
      setErrMessage('Please enter your contact information');
      return;
    }
    try {
      if (event._def.title === 'Car') {
        await bookServiceCity(1, event._def.publicId);
      } else {
        await bookServiceCity(2, event._def.publicId);
      }
    } catch (err) {
      console.log(setErrMessage(err.response.data.message));
    } finally {
      await getAllAvailableTimes();
    }
  };

  useEffect(() => {
    if (contactInformation !== '') {
      setErrMessage('');
    }
  }, [contactInformation]);

  return (
    <Modal centered show={modalShow} onHide={onCloseModal}>
      <Modal.Header className='border-0'>
        <Modal.Title className='ms-2'>
          {response.time !== '' ? (
            <>
              You have successfully booked your service
              <IoIosCheckmarkCircle className='text-success ms-1' />
            </>
          ) : (
            'Confirm service details'
          )}
        </Modal.Title>
      </Modal.Header>
      <Modal.Body className='pt-0'>
        {response.time !== '' ? (
          <ConfirmationBody response={response} />
        ) : isLoading ? (
          <div>Loading...</div>
        ) : (
          <>
            <div className='ms-2'>
              <p className='m-0'>
                <span className='text-muted'>Service time:</span>{' '}
                <span className='fw-bold'>{getReadableDate(event?._instance.range.start)}</span>
              </p>
              <p className='m-0'>
                <span className='text-muted'>Vehicle type:</span> <span className='fw-bold'>{event?.title}</span>
              </p>
              <p className='m-0'>
                <span className='text-muted'>Workshop address:</span>{' '}
                <span className='fw-bold'>{event?._def.extendedProps.address}</span>
              </p>
            </div>
            <div className='mt-3'>
              <label htmlFor='contact' className='form-label ms-2 mb-0'>
                Contact information
              </label>
              <input
                type='text'
                id='contact'
                placeholder='Name, phone'
                className='form-control w-50 mb-2 ms-2'
                required
                maxLength='255'
                value={contactInformation}
                onChange={(e) => setContactInformation(e.target.value)}
              />
              {errMessage !== '' && <p className='err text-danger ms-2 mb-0'>{errMessage}</p>}
            </div>
          </>
        )}
      </Modal.Body>
      <Modal.Footer className='border-0 justify-content-between pt-0 mx-2'>
        <div className='mt-0'>
          <Button onClick={onCloseModal} variant='dark'>
            Close
          </Button>
        </div>
        <div className=''>
          {response.time === '' && (
            <Button variant='primary' onClick={bookService}>
              Confirm service
            </Button>
          )}
        </div>
      </Modal.Footer>
    </Modal>
  );
};

export default ConfirmationModal;
