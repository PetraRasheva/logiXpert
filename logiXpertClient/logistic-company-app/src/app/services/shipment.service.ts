import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Shipment } from '../types/shipment';
import { environment } from '../../environments/environment';
import { ShipmentDetails } from '../types/shipmentDetails';

@Injectable({
  providedIn: 'root'
})
export class ShipmentService {

  constructor(private http: HttpClient) {}

  getShipmentById(id: number): Observable<Shipment> {
    return this.http.get<Shipment>(`${environment.apiUrl}/shipment/find/${id}`, {
      withCredentials: true,
    });
  }

  getShipmentByTrackingNumber(trackingNum: string): Observable<ShipmentDetails> {
    return this.http.get<ShipmentDetails>(`${environment.apiUrl}/shipment/track/${trackingNum}`, {
      withCredentials: true,
    });
  }

  getAllShipments(): Observable<Shipment[]> {
    return this.http.get<Shipment[]>(`${environment.apiUrl}/shipment/all`, {
      withCredentials: true,
    });
  }

  getAllClientShipments(clientId: number): Observable<Shipment[]> {
    return this.http.get<Shipment[]>(`${environment.apiUrl}/shipment/client/${clientId}/shipments`, {
      withCredentials: true,
    });
  }

  createShipment(shipment: Shipment): Observable<Shipment> {
    return this.http.post<Shipment>(`${environment.apiUrl}/shipment/add`, shipment, {
      withCredentials: true,
    });
  }

  updateShipment(shipment: Partial<ShipmentDetails>): Observable<ShipmentDetails> {
    return this.http.put<ShipmentDetails>(`${environment.apiUrl}/shipment/update`, shipment, {
         withCredentials: true
        }
    );
  }

  getNotDeliveredShipments(): Observable<Shipment[]> {
    return this.http.get<Shipment[]>(`${environment.apiUrl}/shipment/not-delivered`, {
      withCredentials: true,
    });
  }

  getUnassignedShipmentsForCourier(courierId: number): Observable<Shipment[]> {
    return this.http.get<Shipment[]>(`${environment.apiUrl}/shipment/courier/${courierId}/unassigned-shipments`, {
      withCredentials: true,
    });
  }

  assignShipmentToCourier2(shipmentId: number, courierId: number): Observable<Shipment> {
    return this.http.put<Shipment>(`${environment.apiUrl}/shipment/${shipmentId}/assign-courier/${courierId}`, {},{
      withCredentials: true,
    }
    );
  }

  assignShipmentToCourier(trackingNumber: string, courierId: number): Observable<Shipment> {
    return this.http.put<Shipment>(`${environment.apiUrl}/shipment/assign-by-tracking/${trackingNumber}/courier/${courierId}`, {},{
      withCredentials: true,
    }
    );
  }

  unassignShipmentFromCourier(trackingNumber: string): Observable<Shipment> {
    return this.http.put<Shipment>(`${environment.apiUrl}/shipment/unassign-by-tracking/${trackingNumber}`, {}, {
      withCredentials: true,
    });
  }
  

  getShipmentsByCourierId(courierId: number): Observable<Shipment[]> {
    return this.http.get<Shipment[]>(`${environment.apiUrl}/shipment/courier/${courierId}`, {
      withCredentials: true,
    });
  }

  deleteShipment(id: number): Observable<void> {
    return this.http.delete<void>(`${environment.apiUrl}/shipment/delete/${id}`, {
      withCredentials: true,
    });
  }

  deleteShipmentByTrackingNumber(trackingNumber: string): Observable<void> {
    return this.http.delete<void>(`${environment.apiUrl}/shipment/delete-by-tracking/${trackingNumber}`, {
      withCredentials: true,
    });
  }

}
