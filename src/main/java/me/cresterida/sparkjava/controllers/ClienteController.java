/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.cresterida.sparkjava.controllers;

import com.google.gson.Gson;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.cresterida.sparkjava.MyErrorMessage;
import me.cresterida.sparkjava.MyMessage;
import me.cresterida.sparkjava.MySuccessMessage;
import me.cresterida.sparkjava.domain.Cliente;
import me.cresterida.sparkjava.domain.EmptyCliente;
import me.cresterida.sparkjava.exceptions.DuplicateClienteException;
import me.cresterida.sparkjava.exceptions.NotFoundException;
import me.cresterida.sparkjava.services.ClienteServices;
import me.cresterida.sparkjava.services.impl.ClienteServicesImpl;
import spark.Request;
import spark.Response;

/**
 *
 * @author kiquetal
 */
public class ClienteController {
    public static MyMessage insertCliente(Request re,Response res)
    {
        MyMessage msg;
        res.header("Content-Type", "application/json");
        Gson g=new Gson();
        Cliente c= g.fromJson(re.body(), Cliente.class);
        ClienteServices cs=new ClienteServicesImpl();
        try
        {
            cs.addCliente(c);
            msg=new MySuccessMessage();
            Map<String,String> map=new HashMap<>();
            map.put("message", "Cliente agregado de forma exitosa");
            msg.setMessage(map);
        }
         
        catch (DuplicateClienteException ex)
        {
             msg=new MyErrorMessage();
            Map<String,String> todo=new HashMap<>();
            todo.put("errorMessage",ex.getMessage());
            msg.setMessage(todo);
            res.status(400);
            System.out.println("specific catch");
            
        }
        catch(Exception ex)
        {
            msg=new MyErrorMessage();
            Map<String,String> todo=new HashMap<>();
            todo.put("errorMessage",ex.toString());
            msg.setMessage(todo);
            res.status(403);
            
        }
        
     return msg;
    }
    
    public boolean existingClient(Cliente c)
    {
     
        return false;
    }
    public static MyMessage getAllClientes(Request req, Response res)
    {

          MyMessage msg;
        res.header("Content-Type", "application/json");
       
         ClienteServices cs=new ClienteServicesImpl();
        try
        {
            List<Cliente> lista = cs.getAllClients();
            msg=new MySuccessMessage();
            Map<String,List<Cliente>> map=new HashMap<>();
            map.put("clientes", lista);
            msg.setMessage(map);
        }
       
        catch(Exception ex)
        {
            msg=new MyErrorMessage();
            Map<String,String> todo=new HashMap<>();
            todo.put("errorMessage",ex.toString());
            msg.setMessage(todo);
            res.status(403);
            
        }
        return msg;
        
        
    }
    public static MyMessage getCliente(Request req,Response res)
    {
        MyMessage msg;
        res.header("Content-type", "application/json");
           ClienteServices pg=new ClienteServicesImpl();
        String clientId=req.params(":clienteId");
         try
        {
            msg=new MySuccessMessage();
            Cliente p=pg.findCliente(Integer.parseInt(clientId));
            
            if (p instanceof EmptyCliente)
                throw new NotFoundException("No se encontro registro con el id Proveido");
            Map<String,String>m=new HashMap<>();
            m.put("pago",p.toString());
            msg.<Cliente>setMessage(p);
        }
        catch (NotFoundException ex)
        {
            msg=new MyErrorMessage();
         Map<String,String>m=new HashMap<>();
         m.put("errorMessage",ex.toString());
         msg.setMessage(m);
         res.status(404);
        }
        catch(Exception ex)
        {
            
         msg=new MyErrorMessage();
         Map<String,String>m=new HashMap<>();
         m.put("errorMessage",ex.toString());
         msg.setMessage(m);
         res.status(500);
            
        }
        
        return msg;
    }
    
    
    
}
