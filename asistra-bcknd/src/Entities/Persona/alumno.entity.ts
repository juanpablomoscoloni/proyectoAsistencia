import { Entity, Column, PrimaryGeneratedColumn, OneToMany, ManyToOne, JoinColumn, OneToOne } from 'typeorm';
import { Inscripcion } from '../Cursada/inscripcion.entity';

@Entity() export class Alumno {

    @PrimaryGeneratedColumn()
    id: number;

    @Column()
    nombre: string;

    @Column()
    apellido: string;

    @Column()
    legajo: string;

    @OneToMany(type => Inscripcion, inscripcion => inscripcion.alumno,{
        cascade: true})
        inscripciones:Inscripcion[];

}