import { Entity, Column, PrimaryGeneratedColumn, OneToMany, ManyToOne, JoinColumn, OneToOne } from 'typeorm';
import { Cursada } from './cursada.entity';

@Entity() export class Comision {

    @PrimaryGeneratedColumn()
    id: number;

    @Column()
    codigo: string;

    @OneToMany(type => Cursada, cursada => cursada.comision,{
        cascade: true})
        cursadas:Cursada[];


}