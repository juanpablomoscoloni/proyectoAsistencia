import { Entity, Column, PrimaryGeneratedColumn, OneToMany, ManyToOne, JoinColumn, OneToOne } from 'typeorm';
import { Inscripcion } from './inscripcion.entity';
import { Asignatura } from './asignatura.entity';
import { Comision } from './comision.entity';
import { Docente } from '../Persona/docente.entity';
import { DiaClase } from './diaclase.entity';

@Entity() export class Cursada {

    @PrimaryGeneratedColumn()
    id: number;

    @Column()
    anio: number;

    @Column()
    faltasMaximas: number;

    @OneToMany(type => Inscripcion, inscripcion => inscripcion.cursada,{
        cascade: true})
        inscripciones:Inscripcion[];

    @ManyToOne(type => Asignatura, asignatura => asignatura.cursadas)      
    asignatura: Asignatura;    

    @ManyToOne(type => Comision, comision => comision.cursadas)      
    comision: Comision;  

    @ManyToOne(type => Docente, docente => docente.cursadas)      
    docente: Docente; 

    @OneToMany(type => DiaClase, diaclase => diaclase.cursada,{
        cascade: true})
        diasclases:DiaClase[]; 

}