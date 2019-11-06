import { DomicilioDTO } from './domicilio.dto';

export class ClienteDTO {
    razonSocial:string;
    cuit:number;
    email:string;
    domicilio:DomicilioDTO;
    user:string;
}