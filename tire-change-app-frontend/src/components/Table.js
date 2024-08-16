import React, { useState } from 'react';
import FullCalendar from '@fullcalendar/react';
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin from '@fullcalendar/interaction';
import timeGridPlugin from '@fullcalendar/timegrid';
import ConfirmationModal from './ConfirmationModal';

const TableComponent = ({ events, getAllAvailableTimes }) => {
  const [modalShow, setModalShow] = useState(false);
  const [selectedEvent, setSelectedEvent] = useState(null);

  const eventClickAction = (info) => {
    setSelectedEvent(info.event);
    setModalShow(true);
  };


  return (
    <div className='calendar'>
      <FullCalendar
        plugins={[dayGridPlugin, interactionPlugin, timeGridPlugin]}
        initialView='timeGridWeek'
        events={events}
        eventBackgroundColor='#378006'
        timeZone='UTC'
        eventClick={eventClickAction}
        headerToolbar={{ left: '', center: 'title', right: 'prev,next timeGridDay,timeGridWeek,dayGridMonth today' }}
        height={'65vh'}
        views={{
          dayGridMonth: {
            showNonCurrentDates: false,
          },
          timeGridDay: {
            slotDuration: '01:00:00',
            slotLabelInterval: '01:00:00',
          },
        }}
        initialDate={new Date()}
        dayMaxEvents={true}
        hiddenDays={[0, 6]}
        navLinks={true}
        slotMinTime='08:00:00'
        slotMaxTime='18:00:00'
        allDaySlot={false}
        eventTimeFormat={{
          hour: '2-digit',
          minute: '2-digit',
          meridiem: 'short',
        }}
        eventDisplay='list-item'
      />
      <ConfirmationModal
        modalShow={modalShow}
        setModalShow={setModalShow}
        event={selectedEvent}
        getAllAvailableTimes={getAllAvailableTimes}
      />
    </div>
  );
};

export default TableComponent;
