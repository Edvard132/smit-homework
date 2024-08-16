import React from 'react';
import Row from 'react-bootstrap/Row';
import { formatDate } from '../utils/date';

const Filter = ({ filters, setFilters, errMessage, setErrMessage }) => {
  const onWorkshopChange = (event) => {
    let filter = event.target.value;
    let updatedFilters = { ...filters };
    console.log(updatedFilters);
    if (updatedFilters.workshop.includes(filter)) {
      console.log(filter);

      updatedFilters.workshop = updatedFilters.workshop.filter((item) => item !== filter);
    } else {
      updatedFilters.workshop.push(filter);
    }
    if (updatedFilters.workshop.length !== 0) {
      setErrMessage('');
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
    let untilDate;

    if (e.target.value !== undefined) {
      const selectedDate = e.target.value;
      if (selectedDate < filters.date.from) {
        setErrMessage('Date cannot be before from date');
      }
      console.log('Selected', selectedDate);
      const today = formatDate(new Date());

      untilDate = selectedDate > today ? selectedDate : today;
    } else {
      untilDate = getDateWeekAhead();
    }

    console.log(filters);
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
                value={'london'}
                checked={filters.workshop.includes('london')}
                className='ms-3 me-1'
              />
              <label htmlFor='london'>London</label>
            </div>
            <div className='d-flex alig-items-center justify-content-end justify-content-xl-start'>
              <input
                type='checkbox'
                id='manchester'
                value={'manchester'}
                onChange={onWorkshopChange}
                checked={filters.workshop.includes('manchester')}
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
                id='car'
                onChange={onVehicleTypeChange}
                value={'car'}
                checked={filters.vehicleType.includes('car')}
                className='ms-3 me-1'
              />
              <label htmlFor='car'>Car</label>
            </div>
            <div className='d-flex alig-items-center'>
              <input
                type='checkbox'
                id='truck'
                value={'truck'}
                onChange={onVehicleTypeChange}
                checked={filters.vehicleType.includes('truck')}
                className='ms-3 me-1'
              />
              <label htmlFor='truck' className=''>
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
              <input
                type='date'
                id='from'
                value={filters.date.from}
                onChange={(e) =>
                  setFilters({
                    ...filters,
                    date: {
                      ...filters.date,
                      from: e.target.value !== undefined ? e.target.value : formatDate(new Date()),
                    },
                  })
                }
              />
            </div>
            <div className='d-flex alig-items-center'>
              <label htmlFor='until' className='ms-3 pe-3 pe-xxl-1'>
                Until
              </label>
              <input
                type='date'
                id='until'
                value={filters.date.until}
                onChange={(e) =>
                  setFilters({
                    ...filters,
                    date: {
                      ...filters.date,
                      until: handleDateChange(e),
                    },
                  })
                }
              />
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
