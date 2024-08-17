import React from 'react';
import Container from 'react-bootstrap/Container';

const NotFound = () => {
  return (
    <Container className='d-flex justify-content-center align-items-center' style={{ minHeight: '80vh' }}>
      <div className='my-auto'>
        <h2>404 - Page Not Found</h2>
        <p>The page you are looking for does not exist.</p>
      </div>
    </Container>
  );
};

export default NotFound;
