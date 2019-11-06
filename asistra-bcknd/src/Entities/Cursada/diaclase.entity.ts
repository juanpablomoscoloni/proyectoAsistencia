import { Entity, Column, PrimaryGeneratedColumn, OneToMany, ManyToOne, JoinColumn, OneToOne } from 'typeorm';
import { Docente } from '../Persona/docente.entity';
import { Cursada } from './cursada.entity';
import { Asistencia } from './asistencia.entity';

@Entity() export class DiaClase {

    @PrimaryGeneratedColumn()
    id: number;

    @Column('date')
    fecha: string;

    @Column()
    tema: string;

    @Column()
    token: string;

    @Column()
    estadoToken: number;

    @ManyToOne(type => Docente, docente => docente.diasclases)      
    docente: Docente; 

    @ManyToOne(type => Cursada, cursada => cursada.diasclases)      
    cursada: Cursada; 

    @OneToMany(type => Asistencia, asistencia => asistencia.diaclase,{
        cascade: true})
        asistencias:Asistencia[]; 

}