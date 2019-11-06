import { Entity, Column, PrimaryGeneratedColumn, OneToMany, ManyToOne, JoinColumn, OneToOne } from 'typeorm';
import { Cursada } from './cursada.entity';

@Entity() export class Asignatura {

    @PrimaryGeneratedColumn()
    id: number;

    @Column()
    nombre: string;

    @OneToMany(type => Cursada, cursada => cursada.asignatura,{
        cascade: true})
        cursadas:Cursada[];

}