import React from 'react';
import Container from 'react-bootstrap/Container';
import Button from 'react-bootstrap/Button';
import { useNavigate } from 'react-router-dom';

const Landing = () => {
    const navigate = useNavigate();

    function goToDashboard() {
        navigate('/book');
    }

  return (
    <Container className='text-center content' style={{ marginTop: '270px' }}>
      <h2>
        At <span className='text-info fw-bolder shadow-sm'>PrimeTyre</span>, we are dedicated to providing top-notch tire services to keep you
        safely on the road. Whether you need a quick tire change, a thorough inspection, or expert advice on the best tires for your
        vehicle, our team of experienced professionals is here to help.
      </h2>
      <div className='mt-5'>
        <Button className='btn-lg mt-1' variant='outline-info' onClick={goToDashboard}>Book Your Service!</Button>
      </div>

    </Container>
  );
};

export default Landing;