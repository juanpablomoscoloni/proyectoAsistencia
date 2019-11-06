import { Entity, Column, PrimaryGeneratedColumn, OneToMany, ManyToOne, JoinColumn, OneToOne } from 'typeorm';
import { DiaClase } from './diaclase.entity';
import { Inscripcion } from './inscripcion.entity';

@Entity() export class Asistencia {

    @PrimaryGeneratedColumn()
    id: number;

    @Column()
    estado: number;

    @ManyToOne(type => DiaClase, diaclase => diaclase.asistencias)      
    diaclase: DiaClase; 

    @ManyToOne(type => Inscripcion, inscripcion => inscripcion.asistencias)      
    inscripcion: Inscripcion; 

}