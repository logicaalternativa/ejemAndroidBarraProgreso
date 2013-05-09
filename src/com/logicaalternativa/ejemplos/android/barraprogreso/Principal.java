/*
 *      Principal.java
 *      
 *      Copyright 2013 Miguel Rafael Esteban Martín <miguel.esteban@logicaalternativa.com>
 *      
 *      This program is free software; you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation; either version 2 of the License, or
 *      (at your option) any later version.
 *      
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *      
 *      You should have received a copy of the GNU General Public License
 *      along with this program; if not, write to the Free Software
 *      Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 *      MA 02110-1301, USA.
 */
package com.logicaalternativa.ejemplos.android.barraprogreso;





import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;

/**
 * Clase de la única actividad de la aplicación
 * @author miguel
 *
 */
public class Principal extends Activity {
	
	/**
	 * Valor en milisegundo de lo que dura la barra de progreso
	 */
	private static final Integer MILI_SEG_DURACIONBARRA = 20000;
	
	private static final Integer MILI_SEG_ACTUALIZACIONBARRA = 500;
	
	/**
	 * Objeto privado barra de progreso 
	 */
	private ProgressBar barraProgreso;
	
	/**
	 * Objeto privado botón de empezar 
	 */
	private Button botonArrancar;
	
	/**
	 *  Objeto de la clase privada {@link CuentaAtras}
	 */
	CuentaAtras cuentaAtras;
	
	
	
	/**
	 * Evento Onclik del botón arrancar
	 */
	OnClickListener botonArrancarListener = new OnClickListener() {
        public void onClick(View view) {
        	arrancarCuentaAtras();
        }
    };
	
	
	

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_principal);

		// Se obtiene la barra de progreso
		barraProgreso = ( ProgressBar )findViewById( R.id.barraProgreso );
		
		// Se obtiene el botón y se añande el evento onclick
		botonArrancar = (Button) findViewById( R.id.inicio );
		botonArrancar.setOnClickListener( botonArrancarListener );	
		
	}
	
	/**
	 * Método privado que arranca la cuenta antras. Crea una 
	 * nueva instancia y la arranca
	 */
	private void arrancarCuentaAtras(){		
		
		if ( cuentaAtras != null ) {
			
			cuentaAtras.cancel();
		}
		
		
		cuentaAtras = new CuentaAtras( MILI_SEG_DURACIONBARRA, MILI_SEG_ACTUALIZACIONBARRA); 
		
		cuentaAtras.start();		
	}
	
	
	
	/**
	 * Evento que se ha configurado en el layout para que se lance
	 * en el onclik del radiobutton
	 * @param view
	 */
	public void onRadioButtonClicked(View view) {
	   
		boolean checked = ((RadioButton) view).isChecked();
	    
	    // Comprueba que radio button ha sido apretado
	    switch(view.getId()) {
	        case R.id.verde_gris:
	            if (checked) {
	            	cambiarColorBarra( R.drawable.dw_verdegris );
	            }
	                
	            break;
	        case R.id.amarillo_azul:
	            if (checked) {
	            	cambiarColorBarra( R.drawable.dw_amarilloazul );
	            }	                
	            break;
	    }
	}
	
	/**
	 * Método que cambia los colores de la barra de progreso.
	 * Ha sido necesario hacer la 'ñapa' de obtener el objeto
	 * {@link Rect} antes de cambiar los colores y después volver
	 * a asignarlo
	 * @param id
	 */
	private void cambiarColorBarra( int id ){
		
		Rect bounds = barraProgreso.getProgressDrawable().getBounds();
    	barraProgreso.setProgressDrawable(getResources().getDrawable( id ) );
    	barraProgreso.getProgressDrawable().setBounds(bounds);
    	
    	arrancarCuentaAtras();
		
	}
	
	/**
	 * Método privado que sirve para calcula el porcentaje de la barra de progreso
	 * valor entre 1 y 100
	 * @param quedaMilisegundos
	 * @return
	 */
	private int damePorcentajeProgreso( long quedaMilisegundos ) {	
		
		long milisg = quedaMilisegundos + ( MILI_SEG_ACTUALIZACIONBARRA / 2 );
		
		milisg = ( milisg / MILI_SEG_ACTUALIZACIONBARRA ) * MILI_SEG_ACTUALIZACIONBARRA;		
		
		Long res = (  ( MILI_SEG_DURACIONBARRA - milisg  )  * 100 ) / MILI_SEG_DURACIONBARRA;
		
		return res.intValue();
		
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_pricipal, menu);
		return true;
	}
	
	
	/**
	 * Clase privada que extiende de {@link CountDownTimer} que sirve como 
	 * ejemplo para mostrar la barra de progreso
	 * @author miguel
	 *
	 */
	private class CuentaAtras  extends CountDownTimer {

		/**
		 * Constructor
		 * @param millisInFuture
		 * @param countDownInterval
		 */
		public CuentaAtras(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
			
		}	
		
		
		
		/* (non-Javadoc)
		 * @see android.os.CountDownTimer#onTick(long)
		 */
		@Override
		public void onTick(long quedaMilisegundos) {

			int progresoBarra = damePorcentajeProgreso( quedaMilisegundos );	
			
			barraProgreso.setProgress( progresoBarra );
		}
		
		/* (non-Javadoc)
		 * @see android.os.CountDownTimer#onFinish()
		 */
		@Override
		public void onFinish() {
			
			barraProgreso.setProgress( 100 );
			
		}
    	
    }

}
