import { Cuenta } from './Entities/Persona/cuenta.entity';
import { Module } from '@nestjs/common';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { TypeOrmModule } from '@nestjs/typeorm';
import { Alumno } from './Entities/Persona/alumno.entity';
import { Docente } from './Entities/Persona/docente.entity';
import { Asignatura } from './Entities/Cursada/asignatura.entity';
import { Asistencia } from './Entities/Cursada/asistencia.entity';
import { Comision } from './Entities/Cursada/comision.entity';
import { Cursada } from './Entities/Cursada/cursada.entity';
import { DiaClase } from './Entities/Cursada/diaclase.entity';
import { Inscripcion } from './Entities/Cursada/inscripcion.entity';

@Module({
  imports: [
    TypeOrmModule.forRoot({
      type: 'mysql',
      host: 'localhost',
      port: 3306,
      username: 'root',
      database: 'asistra',
      entities: [__dirname + '/**/*.entity{.ts,.js}'],
      synchronize: true,
    }), TypeOrmModule.forFeature([Alumno, Docente, Cuenta, Asignatura, Asistencia, Comision, Cursada, DiaClase, Inscripcion])],
  controllers: [AppController],
  providers: [AppService],
})
export class AppModule {}
