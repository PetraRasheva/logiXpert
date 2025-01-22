import { Client } from "./client";
import { Employee } from "./еmployee";

export interface ShipmentDetails {
    id: number;
    trackingNumber: string;
    weight: number;
    price: number;
    deliveryStatus: string;
    shipmentDate: string; 
    deliveryDate: string | null; 
    source: string;
    destination: string;
    sender: Client;
    receiver: Client;
    owner: Client | Employee;
    courierName: string | null;
    courierId: number | null;
    ownerId?: number;
    profit?: number;
  }