import React, { useEffect, useState } from 'react';
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Filter from '../components/Filter';
import api from '../api/axiosConfig';
import { formatDate } from '../utils/date';
import { getDateWeekAhead } from '../utils/date';
import TableComponent from '../components/Table';

const Dashboard = () => {
  const [events, setEvents] = useState([]);
  const [errMessage, setErrMessage] = useState('');
  const [isLoading, setIsLoading] = useState(false);

  const [filters, setFilters] = useState({
    workshop: ['london', 'manchester'],
    vehicleType: ['car', 'truck'],
    date: {
      from: formatDate(new Date()),
      until: getDateWeekAhead(),
    },
  });

  const getAvailableTimes = async () => {
    try {
      const response = await api.get(
        `/api/v1/workshops/3/availableTimes?from=${filters.date.from}&until=${filters.date.until}`
      );

      const data = await response.data;
      console.log(data)
      const formattedEvents = data.map((item) => ({
        id: item.uuid !== null ? item.uuid : item.id,
        title: item.vehicleType,
        start: item.time,
        address: item.address,
        backgroundColor: 'black'
      }));

      setEvents(formattedEvents);
    } catch (err) {
      console.log(err);
    }
  };



  useEffect(() => {
    getAvailableTimes();
  }, [filters]);

  return (
    <Container className='content bg-light text-black' fluid>
      <Container className='mt-5'>
        <h2 className='fw-bold'>Book your service in seconds!</h2>
        <Row>
          <Filter filters={filters} setFilters={setFilters} errMessage={errMessage} setErrMessage={setErrMessage} />
        </Row>
        {errMessage === '' && isLoading ? (
          <div className='text-center mt-5 fs-4 text-muted'>Loading...</div>
        ) : (
          <>
            <hr className='text-muted my-4' />
            <Row className='mt-3'>
              <div>
                <TableComponent events={events} getAllAvailableTimes={getAvailableTimes} />
              </div>
            </Row>
          </>
        )}
      </Container>
    </Container>
  );
};

export default Dashboard;
