import React from 'react';
import Modal from 'react-bootstrap/Modal';
const ConfirmationBody = ({ response }) => {
  return (
    <Modal.Body className='pt-0 ps-0 ms-2'>
      <div className='ps-0'>
        <p className='mb-2 fw-bold pt-0'>Booking details</p>
        <ul className='ps-0 py-0 mb-0 text-muted'>
          <li>
            Time: <span className='fw-bolder'>{response.time}</span>
          </li>{' '}
          <li>
            Address: <span className='fw-bolder'>{response.address}</span>
          </li>{' '}
          <li>
            Vehicle Type: <span className='fw-bolder'>{response.vehicleType}</span>
          </li>
        </ul>
        <p className='fst-italic text-secondary mt-3 mb-0'>Thank you for choosing PrimeTyre</p>
      </div>
    </Modal.Body>
  );
};

export default ConfirmationBody;
