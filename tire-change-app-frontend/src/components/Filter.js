import React from 'react';
import Row from 'react-bootstrap/Row';
import { formatDate } from '../utils/date';

const Filter = ({ filters, setFilters, errMessage, setErrMessage, dateFilters, setDateFilters }) => {
  const onWorkshopChange = (event) => {
    let filter = event.target.value;
    let updatedFilters = { ...filters };
    if (updatedFilters.workshop.includes(filter)) {
      updatedFilters.workshop = updatedFilters.workshop.filter((item) => item !== filter);
    } else {
      updatedFilters.workshop.push(filter);
    }
    if (updatedFilters.workshop.length !== 0) {
      setErrMessage('');
    } else {
      setErrMessage('Select at least one workshop');
    }
    setFilters(updatedFilters);
  };

  const onVehicleTypeChange = (event) => {
    let filter = event.target.value;
    let updatedFilters = { ...filters };

    if (updatedFilters.vehicleType.includes(filter)) {
      updatedFilters.vehicleType = updatedFilters.vehicleType.filter((item) => item !== filter);
    } else {
      updatedFilters.vehicleType.push(filter);
    }
    if (updatedFilters.vehicleType.length !== 0) {
      setErrMessage('');
    } else {
      setErrMessage('Select at least one vehicle type');
    }
    setFilters(updatedFilters);
  };

  const handleDateChange = (e) => {
    const { id, value } = e.target;
    let updatedFilters = { ...dateFilters };

    if (id === 'from') {
      if (value > dateFilters.until) {
        setErrMessage('"From" date cannot be after or equal to "Until" date');
        updatedFilters.from = value;
      } else {
        setErrMessage('');
        updatedFilters.from = value;
      }
    } else if (id === 'until') {
      if (value < dateFilters.from) {
        setErrMessage('"Until" date cannot be before or equal to "From" date');
        updatedFilters.until = value;
      } else {
        setErrMessage('');
        updatedFilters.until = value;
      }
    }
    setDateFilters(updatedFilters);
  };

  return (
    <Row className='mt-3 '>
      <h4 className='text-muted'>Filter results</h4>
      <div className='d-flex mt-1 flex-column align-items-start flex-xl-row align-items-xl-center justify-content-start gap-3 gap-xl-0'>
        <div className='d-flex align-items-center'>
          <h4 className='pe-1'>Workshop</h4>

          <Row className=''>
            <div className='d-flex alig-items-center justify-content-end justify-content-xl-start'>
              <input
                type='checkbox'
                id='london'
                onChange={onWorkshopChange}
                value={'1A Gunton Rd, London'}
                checked={filters.workshop.includes('1A Gunton Rd, London')}
                className='ms-3 me-1'
              />
              <label htmlFor='london'>London</label>
            </div>
            <div className='d-flex alig-items-center justify-content-end justify-content-xl-start'>
              <input
                type='checkbox'
                id='manchester'
                value={'14 Bury New Rd, Manchester'}
                onChange={onWorkshopChange}
                checked={filters.workshop.includes('14 Bury New Rd, Manchester')}
                className='ms-3 me-1'
              />
              <label htmlFor='manchester' className=''>
                Manchester
              </label>
            </div>
          </Row>
        </div>

        <div className='d-flex align-items-center'>
          <h4>Vehicle type</h4>

          <Row>
            <div className='d-flex alig-items-center'>
              <input
                type='checkbox'
                id='Car'
                onChange={onVehicleTypeChange}
                value={'Car'}
                checked={filters.vehicleType.includes('Car')}
                className='ms-3 me-1'
              />
              <label htmlFor='Car'>Car</label>
            </div>
            <div className='d-flex alig-items-center'>
              <input
                type='checkbox'
                id='Car/Truck'
                value={'Car/Truck'}
                onChange={onVehicleTypeChange}
                checked={filters.vehicleType.includes('Car/Truck')}
                className='ms-3 me-1'
              />
              <label htmlFor='Car/Truck' className=''>
                Truck
              </label>
            </div>
          </Row>
        </div>

        <div className='d-flex align-items-center'>
          <h4 className=''> Select dates</h4>

          <div className='d-flex'>
            <div className='d-flex'>
              <label htmlFor='from' className='ms-3 pe-3 pe-xxl-1'>
                From
              </label>
              <input type='date' id='from' value={dateFilters.from} onChange={handleDateChange} />
            </div>
            <div className='d-flex alig-items-center'>
              <label htmlFor='until' className='ms-3 pe-3 pe-xxl-1'>
                Until
              </label>
              <input type='date' id='until' value={dateFilters.until} onChange={handleDateChange} />
            </div>
          </div>
        </div>
      </div>
      <div className='text-center mt-3'>
        {errMessage !== '' && <p className='err text-danger p-0 mb-0'>{errMessage}</p>}
      </div>
    </Row>
  );
};

export default Filter;
