import VendorEventClient from '../api/vendorEventClient';
import BindingClass from '../util/bindingClass';
import Header from '../components/header';
import DataStore from '../util/DataStore';

class VendorAccount extends BindingClass {
    constructor() {
        super();
        this.bindClassMethods(['mount', 'loadVendorData'], this);
        this.client = new VendorEventClient();
        this.header = new Header(this.dataStore);
    }

    mount() {
    this.header.addHeaderToPage();
        this.loadVendorData();
    }

    async loadVendorData() {
        const urlParams = new URLSearchParams(window.location.search);
        const vendorId = urlParams.get('id');
        const vendorName = urlParams.get('name');

        try {
            console.log('Calling API...'); // logging before API call
            const vendorData = await this.client.getVendor(vendorId, vendorName);

            if (!vendorData) {
                console.error('Vendor data is undefined.');
                return;
            }

            console.log('Received response:', vendorData); // logging after receiving response

            document.getElementById('vendor-name').textContent = vendorData.name ? vendorData.name : 'N/A';
            document.getElementById('vendor-bio').textContent = vendorData.bio ? vendorData.bio : 'N/A';
            document.getElementById('vendor-eventIds').textContent = vendorData.eventIds ? vendorData.eventIds.join(', ') : 'N/A';
            document.getElementById('vendor-tags').textContent = vendorData.tags ? vendorData.tags.join(', ') : 'N/A';
        } catch (error) {
            console.error('Error fetching vendor data:', error);
            return;
        }
    }
}

const main = async () => {
    const vendorAccount = new VendorAccount();
    vendorAccount.mount();
};

window.addEventListener('DOMContentLoaded', main);
