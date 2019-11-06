import { ItemPedidoDTO } from './itemPedido.dto';

export class PedidoDTO {
    numero:number;
    fecha:string;
    total:Float32Array;
    cuitCliente:number;
    idEstado:number;
    items:ItemPedidoDTO[]
}