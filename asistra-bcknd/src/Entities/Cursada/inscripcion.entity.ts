import { Entity, Column, PrimaryGeneratedColumn, OneToMany, ManyToOne, JoinColumn, OneToOne } from 'typeorm';
import { Alumno } from '../Persona/alumno.entity';
import { Cursada } from './cursada.entity';
import { Asistencia } from './asistencia.entity';

@Entity() export class Inscripcion {

    @PrimaryGeneratedColumn()
    id: number;

    @Column()
    inasistencias: number;

    @ManyToOne(type => Alumno, alumno => alumno.inscripciones)      
    alumno: Alumno;

    @ManyToOne(type => Cursada, cursada => cursada.inscripciones)      
    cursada: Cursada;

    @OneToMany(type => Asistencia, asistencia => asistencia.inscripcion,{
        cascade: true})
        asistencias:Asistencia[]; 

}