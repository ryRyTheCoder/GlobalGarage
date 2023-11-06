import axios from "axios";
import BindingClass from "../util/bindingClass";
import Authenticator from "./authenticator";
/**
 * Client to call the VendorEventService.
 */
export default class VendorEventClient extends BindingClass {
    constructor(props = {}) {
        super();
        const methodsToBind = ['clientLoaded','getVendor', 'getIdentity', 'logout','login','getAllVendors', 'getAllEvents', 'getOneEvent', 'createVendor'];
        this.bindClassMethods(methodsToBind, this);
        this.authenticator = new Authenticator();
        this.props = props;
        axios.defaults.baseURL = process.env.API_BASE_URL;
        this.axiosClient = axios;
        this.clientLoaded();
    }
    clientLoaded() {
        if (this.props.hasOwnProperty("onReady")) {
            this.props.onReady(this);
        }
    }

    /**
     * Get the identity of the current user
     * @param errorCallback (Optional) A function to execute if the call fails.
     * @returns The user information for the current user.
     */
    async getIdentity(errorCallback) {
        try {
            const isLoggedIn = await this.authenticator.isUserLoggedIn();

            if (!isLoggedIn) {
                return undefined;
            }

            return await this.authenticator.getCurrentUserInfo();
        } catch (error) {
            this.handleError(error, errorCallback)
        }
    }

    async login() {
        this.authenticator.login();
    }

    async logout() {
        this.authenticator.logout();
    }

    async getTokenOrThrow(unauthenticatedErrorMessage) {
        const isLoggedIn = await this.authenticator.isUserLoggedIn();
        if (!isLoggedIn) {
            throw new Error(unauthenticatedErrorMessage);
        }

        return await this.authenticator.getUserToken();
    }

    /**
     * Gets the playlist for the given ID.
     * @param id Unique identifier for a playlist
     * @param errorCallback (Optional) A function to execute if the call fails.
     * @returns The playlist's metadata.
     */
    /**
         * Get vendor.
         * @param errorCallback (Optional) A function to execute if the call fails.
         * @returns The list of all vendors.
         */
    async getVendor(id, name, errorCallback) {
        try {
            const response = await this.axiosClient.get(`vendors/${id}/${name}`);
            return response.data;
        } catch (error) {
            this.handleError(error, errorCallback)
        }
    }
//async getVendor(id, name, errorCallback) {
//    try {
//        const response = await this.axiosClient.get(`vendors/${id}/${name}`);
//        const data = response.data;
//        const result = {
//            id: data.id,
//            name: data.name,
//            bio: data.bio,
//            eventIds: data.eventIds,
//            tags: data.tags
//        };
//        return result;
//    } catch (error) {
//        this.handleError(error, errorCallback)
//    }
//}
async createVendor(vendorDetails, errorCallback) {
    try {
        const token = await this.getTokenOrThrow("Only authenticated users can create vendors.");

        const payload = {
            name: vendorDetails.name,
            bio: vendorDetails.bio,
            tags: vendorDetails.tags
        };

        const response = await this.axiosClient.post('/vendor', payload, {
            headers: {
                Authorization: `Bearer ${token}`
            }
        });

        return response.data;
    } catch (error) {
        this.handleError(error, errorCallback);
    }
}
    /**
     * Get all vendors.
     * @param parameter1 Unique identifier for a vendor partition key
     * @param parameter2 Unique name for a vendor range key
     * @param errorCallback (Optional) A function to execute if the call fails.
     * @returns The list of all vendors.
     */
    async getAllVendors(parameter1, parameter2, errorCallback) {
        try {
            const queryParams = {id: parameter1, name: parameter2};

            const response = await this.axiosClient.get('vendors', { params: queryParams });

            console.log("Server Response:", response.data);

            const result = {
                vendors: response.data.vendorList,
                currentId: parameter1,
                currentName: parameter2,
                nextId: response.data.id,
                nextName: response.data.name
            };
            return result;
        }
        catch (error) {
            this.handleError(error, errorCallback);
        }
    }

        /**
         * Get all events.
         * @param errorCallback (Optional) A function to execute if the call fails.
         * @returns The list of all events.
         */
        async getAllEvents(parameter1, parameter2, errorCallback) {
            try {
                const queryParams = {};

                if (parameter1) {
                    queryParams.id = parameter1;
                }

                if (parameter2) {
                    queryParams.date = parameter2;
                }

                const response = await this.axiosClient.get('events', { params: queryParams });

                const result = {
                    events: response.data.eventList,
                    currentId: parameter1,
                    currentDate: parameter2,
                    nextId: response.data.id,
                    nextDate: response.data.date
                };
                return result;
            } catch (error) {
                this.handleError(error, errorCallback);
            }
        }

        /**
         * Get one event.
         * @param errorCallback (Optional) A function to execute if the call fails.
         * @returns The list of one event.
         */
    async getOneEvent(id, date, errorCallback) {
        try {
            const response = await this.axiosClient.get(`event/${id}/${date}`);
            const result = {
                id: id,
                date: date,
                name: response.data.event.name,
                location: response.data.event.location,
                isActive: response.data.event.isActive,
                price: response.data.event.price,
                vendors: response.data.event.vendorList,
                tags: response.data.event.tags
            };
            return result;
        } catch (error) {
            this.handleError(error, errorCallback);
        }
    }
    /**
     * Helper method to log the error and run any error functions.
     * @param error The error received from the server.
     * @param errorCallback (Optional) A function to execute if the call fails.
     */
    handleError(error, errorCallback) {
        console.error(error);
        const errorFromApi = error?.response?.data?.error_message;
        if (errorFromApi) {
            console.error(errorFromApi)
            error.message = errorFromApi;
        }
        if (errorCallback) {
            errorCallback(error);
        }
    }
}