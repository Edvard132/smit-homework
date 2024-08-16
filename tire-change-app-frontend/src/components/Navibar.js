import Button from 'react-bootstrap/Button';
import Container from 'react-bootstrap/Container';
import Navbar from 'react-bootstrap/Navbar';
import { useNavigate } from 'react-router-dom';
import { PiTireBold } from 'react-icons/pi';

const Navibar = () => {
  const navigate = useNavigate();
  const toHome = () => {
    navigate('/');
  };

  return (
    <Navbar expand='lg'>
      <Container fluid>
        <Navbar.Brand className='text-info d-flex align-items-center' style={{ cursor: 'pointer' }} onClick={toHome}>
          <PiTireBold className='mx-1'/>
          PrimeTyre Workshop
        </Navbar.Brand>
        {/* <Navbar.Toggle aria-controls='navbarScroll' />
        <Navbar.Collapse id='navbarScroll' className='justify-content-end'>
          
        </Navbar.Collapse> */}
      </Container>
    </Navbar>
  );
};

export default Navibar;
