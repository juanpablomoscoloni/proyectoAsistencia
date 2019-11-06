import { Entity, Column, PrimaryGeneratedColumn, OneToMany, ManyToOne, JoinColumn, OneToOne } from 'typeorm';
import { Cursada } from '../Cursada/cursada.entity';
import { DiaClase } from '../Cursada/diaclase.entity';

@Entity() export class Docente {

    @PrimaryGeneratedColumn()
    id: number;

    @Column()
    nombre: string;

    @Column()
    apellido: string;

    @Column()
    legajo: string;

    @OneToMany(type => Cursada, cursada => cursada.docente,{
        cascade: true})
        cursadas:Cursada[];

    @OneToMany(type => DiaClase, diaclase => diaclase.docente,{
        cascade: true})
        diasclases:DiaClase[];        

}