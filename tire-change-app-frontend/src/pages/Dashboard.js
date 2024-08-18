import React, { useEffect, useState } from 'react';
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Filter from '../components/Filter';
import api from '../api/axiosConfig';
import { formatDate } from '../utils/date';
import { getDate2WeekAhead } from '../utils/date';
import TableComponent from '../components/Table';
import useDebounce from '../hooks/useDebounce';

const Dashboard = () => {
  const [events, setEvents] = useState([]);
  const [filteredEvents, setFilteredEvents] = useState([]);
  const [errMessage, setErrMessage] = useState('');
  const [isLoading, setIsLoading] = useState(false);

  const [filters, setFilters] = useState({
    workshop: ['1A Gunton Rd, London', '14 Bury New Rd, Manchester'],
    vehicleType: ['Car', 'Car/Truck'],
  });
  const [dateFilters, setDateFilters] = useState({
    from: formatDate(new Date()),
    until: getDate2WeekAhead(),
  });

  const debouncedFilters = useDebounce(dateFilters, 100);

  const getAvailableTimes = async () => {
    if (errMessage !== '') {
      return;
    }
    try {
      const response = await api.get(
        `/api/v1/workshops/3/availableTimes?from=${dateFilters.from}&until=${dateFilters.until}`
      );

      const data = await response.data;
      const formattedEvents = data.map((item) => ({
        id: item.uuid !== null ? item.uuid : item.id,
        title: item.vehicleType,
        start: item.time,
        address: item.address,
        backgroundColor: item.vehicleType === 'Car' ? '#0d1944' : '#0c141c',
      }));
      console.log(formattedEvents);

      setEvents(formattedEvents);
      filterEvents();
    } catch (err) {
      console.log(err);
    } finally {
      setIsLoading(false);
    }
  };

  const filterEvents = () => {
    const filteredEventsss = events.filter((event) => {
      const isVehicleTypeMatched = filters.vehicleType.some((type) => event.title.includes(type));
      const isWorkshopMatched = filters.workshop.includes(event.address);
      return isVehicleTypeMatched && isWorkshopMatched;
    });
    setFilteredEvents(filteredEventsss);
  };

  useEffect(() => {
    getAvailableTimes();
  }, [debouncedFilters]);

  useEffect(() => {
    filterEvents();
  }, [events, filters]);

  return (
    <Container className='content bg-light text-black' fluid>
      <Container className='mt-5'>
        <h2 className='fw-bold'>Book your service in seconds!</h2>
        <Row>
          <Filter
            filters={filters}
            setFilters={setFilters}
            dateFilters={debouncedFilters}
            setDateFilters={setDateFilters}
            errMessage={errMessage}
            setErrMessage={setErrMessage}
          />
        </Row>
        {errMessage === '' && isLoading && filteredEvents.length === 0 ? (
          <div className='text-center mt-5 fs-4 text-muted'>Loading...</div>
        ) : (
          <>
            <hr className='text-muted my-4' />
            <Row className='mt-3'>
              <div>
                <TableComponent events={filteredEvents} getAllAvailableTimes={getAvailableTimes} />
              </div>
            </Row>
          </>
        )}
      </Container>
    </Container>
  );
};

export default Dashboard;
