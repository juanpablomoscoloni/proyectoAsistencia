import { Entity, Column, PrimaryGeneratedColumn, OneToMany, ManyToOne, JoinColumn, OneToOne } from 'typeorm';
import { Alumno } from './alumno.entity';
import { Docente } from './docente.entity';

@Entity() export class Cuenta {

    @PrimaryGeneratedColumn()
    id: number;
  
    @Column()
    usuario: string;
    
    @Column()
    contrasena: string;

    @Column()
    rol: string;

    @OneToOne(type => Alumno, { nullable: true })
    @JoinColumn()
    alumno: Alumno;

    @OneToOne(type => Docente, { nullable: true })
    @JoinColumn()
    docente: Docente;
    
}