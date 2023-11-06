import VendorEventClient from '../api/vendorEventClient';
import Header from '../components/header';
import BindingClass from '../util/bindingClass';
import DataStore from '../util/DataStore';

/**
 * Logic needed for the view all Events page of the website.
 */
class ViewAllEvents extends BindingClass {
    // Constructor
    constructor() {
        super();
        this.bindClassMethods(['clientLoaded', 'mount', 'displayEvents', 'nextPageForEvents', 'previousPageForEvents'], this);
        this.client = new VendorEventClient();
        this.dataStore = new DataStore();
        this.dataStore.addChangeListener(this.displayEvents);
        this.header = new Header(this.dataStore);
        this.previousKeys = [];
        console.log("viewAllEvents constructor");
    }

    /**
    * Once the client is loaded, get the event metadata and event list.
    */
    async clientLoaded() {
        document.getElementById('events').innerText = "Loading Events ...";

        const urlParams = new URLSearchParams(window.location.search);
        const id = urlParams.get('id');
        const date = urlParams.get('date');

        const result = await this.client.getAllEvents(id, date);
        console.log("Result:", result);
        const events = result.events;
        console.log("Received events:", events);

        this.previousKeys.push({id: result.currentId, date: result.currentDate});

        this.dataStore.set('events', events);
        this.dataStore.set('previousId', result.currentId);
        this.dataStore.set('previousDate', result.currentDate);
        this.dataStore.set('nextId', result.nextId);
        this.dataStore.set('nextDate', result.nextDate);

        this.displayEvents();
    }

    // Mount method to initialize the page
     mount() {
        this.header.addHeaderToPage();
        this.clientLoaded();
        document.getElementById('next-page').addEventListener('click', this.nextPageForEvents);
        document.getElementById('previous-page').addEventListener('click', this.previousPageForEvents);
        }

    // Gets the next events for the next page to be called in displayEvents
    async nextPageForEvents() {
        document.getElementById('events').innerText = "Loading Events ...";
        // To make the it stop at the end rather than looping around
        if (this.dataStore.get('events') == 0) {
            this.displayVendors();
        }
        else{
        const nextId = this.dataStore.get('nextId');
        const nextDate = this.dataStore.get('nextDate');

        const result = await this.client.getAllEvents(nextId, nextDate);
        console.log("Result: ", result);
        const events = result.events;
        console.log("Received events:", events);

        this.previousKeys.push({id: this.dataStore.get('previousId'), name: this.dataStore.get('previousDate')});

        this.dataStore.set('events', events);
        this.dataStore.set('previousId', result.currentId);
        this.dataStore.set('previousDate', result.previousDate);
        this.dataStore.set('nextId', result.nextId);
        this.dataStore.set('nextDate', result.nextDate);
        }
    }

    // Gets the previous events for the previous page to be called in displayEvents
    async previousPageForEvents() {
        document.getElementById('events').innerText = "Loading Events ...";

        let result;
            if (this.previousKeys.length > 0) {
                const previousRequest = this.previousKeys.pop();
                result = await this.client.getAllEvents(previousRequest.id, previousRequest.date);
            } else {
                result = await this.client.getAllEvents(this.dataStore.get('previousId'), this.dataStore.get('previousDate'));
            }
        console.log("Result:", result);
        const events = result.events;
        console.log("Received events:", events);

        this.dataStore.set('events', events);
        this.dataStore.set('previousId', result.currentId);
        this.dataStore.set('previousDate', result.currentDate);
        this.dataStore.set('nextId', result.nextId);
        this.dataStore.set('nextDate', result.nextDate);
    }
    // Display Events method to loop through each event in the eventsList and display the event
     displayEvents() {
        document.getElementById('events').innerText = "Loading Events ...";

        const events = this.dataStore.get('events');
        const displayDiv = document.getElementById('events');
        displayDiv.innerText = events.length > 0 ? "" : "No events available.";

     events.forEach(event => {
        const eventCard = document.createElement('section');
        eventCard.className = 'eventCard';

        const eventId = encodeURIComponent(event.id);
        const date = encodeURIComponent(event.date);

        const currentHostname = window.location.hostname;

        const isLocal = currentHostname === 'localhost' || currentHostname === '127.0.0.1';
        const baseUrl = isLocal ? 'http://localhost:8000/' : 'https://d3hqn9u6ae71hc.cloudfront.net/';

        const eventPageUrl = `${baseUrl}oneEvent.html?id=${eventId}&date=${date}`;

        const eventName = document.createElement('h2');
        eventName.innerText = event.name;

        const eventDate = document.createElement('h3');
        eventDate.innerText = formatDate(event.date);

        const eventTags = document.createElement('h3');
        eventTags.innerText = event.tags.join(', ');

        eventCard.appendChild(eventName);
        eventCard.appendChild(eventDate);
        eventCard.appendChild(eventTags);

        displayDiv.appendChild(eventCard);

        eventCard.addEventListener('click', () => {
             window.location.href = eventPageUrl;
             console.log('Created Event listener' + eventPageUrl);
        });
     });
  }
}
    function formatDate(dateStr) {
        const dateObj = new Date(dateStr);
        return dateObj.toLocaleString('en-US', {
            month: 'long',
            day: 'numeric',
            year: 'numeric',
            hour: '2-digit',
            hour12: true
        });
    }
/**
 * Main method to run when the page contents have loaded.
 */
 const main = async () => {
    const viewAllEvents = new ViewAllEvents();
    viewAllEvents.mount();
 };

 window.addEventListener('DOMContentLoaded', main);
