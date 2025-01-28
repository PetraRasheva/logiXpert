import { Client } from "./client";
import { Employee } from "./еmployee";

export interface CreateShipment {
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
    ownerId?: number;
    profit?: number;
    companyId: number; 
  }